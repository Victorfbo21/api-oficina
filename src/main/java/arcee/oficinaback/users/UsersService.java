package arcee.oficinaback.users;


import arcee.oficinaback.configs.AppResponse;
import arcee.oficinaback.infra.providers.email.SendEmailService;
import arcee.oficinaback.infra.providers.uploads.UploadsService;
import arcee.oficinaback.password_recovery.PasswordRecovery;
import arcee.oficinaback.password_recovery.PasswordRecoveryRepository;
import arcee.oficinaback.users.dtos.CreateUserDto;
import arcee.oficinaback.users.dtos.UpdatePasswordDto;
import arcee.oficinaback.users.dtos.UploadProfileImageDto;
import arcee.oficinaback.users.records.UpdatePasswordRecord;
import arcee.oficinaback.users.records.UploadProfileImageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class UsersService {


    private final BCryptPasswordEncoder _passwordEncoder;
    private final UsersRepository _usersRepository;

    private final SendEmailService _emailService;

    private  final UploadsService _uploadsService;

    private final PasswordRecoveryRepository _passwordRecoveryRepository;

    public UsersService(BCryptPasswordEncoder passwordEncoder,
                        UsersRepository usersRepository,
                        SendEmailService emailService,
                        UploadsService uploads,
                        PasswordRecoveryRepository passwordRecoveryRepository
                        )
    {
        this._passwordEncoder = passwordEncoder;
        this._usersRepository = usersRepository;
        this._emailService = emailService;
        this._uploadsService = uploads;
        this._passwordRecoveryRepository = passwordRecoveryRepository;
    }


    public List<users_entity> getAllUsers(){
        return _usersRepository.findByIsDeletedFalse();
    }


    public ResponseEntity<AppResponse> createUser(CreateUserDto user){
        var findByEmail = _usersRepository.findByEmail(user.getEmail());

        if(findByEmail.isPresent()){
            return ResponseEntity
                    .status(400)
                    .body(new AppResponse(
                            null,
                            true,
                            400,
                            "Usuário Já Cadastrado!"));
        }

        var toCreateUser = new users_entity();
        BeanUtils.copyProperties(user, toCreateUser);
        toCreateUser.setName(user.getName());
        toCreateUser.setEmail(user.getEmail());
        toCreateUser.setContact_number(user.getContact_number());
        toCreateUser.setProfile_image("");
        toCreateUser.setPassword(_passwordEncoder.encode(user.getPassword()));
        _usersRepository.save(toCreateUser);
        return ResponseEntity.ok(new AppResponse(toCreateUser,false,201,"criado com sucesso!"));
    }

    public ResponseEntity<AppResponse> deleteUser(String toDeleteId, JwtAuthenticationToken token) {
        System.out.println(toDeleteId);
        Optional<users_entity> user = _usersRepository.findById(toDeleteId);

        if(user.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        users_entity newUser = user.get();

        var now  = Instant.now();

        newUser.setDeleteAt(Date.from(now));
        newUser.setDeleted(true);
        newUser.setDeleteBy(token.getName());

        _usersRepository.save(newUser);

        return ResponseEntity.status(200).body(new AppResponse(
                null,
                false,
                200,
                "Deletado com Sucesso!"));

    }

    public ResponseEntity<AppResponse> sendPasswordRecovery(String email){
        Optional<users_entity> user = _usersRepository.findByEmail(email);

        if(user.isEmpty()){
            return ResponseEntity
                    .status(400)
                    .body(new AppResponse(
                    null,
                    true,
                    400,
                    "Usuario Não Encontrado"));
        }

        Optional<PasswordRecovery> verifyRecovery = _passwordRecoveryRepository.findRecoveryByOwnerIdAndIsActive(
                user.get().getId(),
                true);

        if(verifyRecovery.isPresent()){
            return ResponseEntity
                    .status(400)
                    .body(new AppResponse(
                            null,
                            true,
                            400,
                            "Já Recuperação Ativa Para Este Usuário"));
        }

        String code = this.generateRandomCode();

        boolean sendEmailResult = _emailService.sendPasswordRecoveryEmail(email,code);

        if(!sendEmailResult){
            return ResponseEntity
                    .status(500)
                    .body(new AppResponse(
                    null,
                    true,
                    400,
                    "Erro ao Enviar Email"));
        }

        PasswordRecovery recovery = new PasswordRecovery();

        Instant now = Instant.now();

        recovery.setOwnerId(user.get().getId());
        recovery.setRecoveryCode(code);
        recovery.setCreatedAt(Date.from(now));

        _passwordRecoveryRepository.save(recovery);

        return ResponseEntity
                .ok(
                new AppResponse(
                null,false,200,
                "Solicitação Enviada com Sucesso"));
    }

    private String generateRandomCode(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 6; i++){
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }

    public ResponseEntity<AppResponse> uploadProfileImage(UploadProfileImageDto file) throws IOException {

        UploadProfileImageResponse result = _uploadsService.uploadProfileImage(file);

        if(result.error()){
            return ResponseEntity.status(500)
                    .body(new AppResponse(
                            null,
                            true,
                            500,
                            "Erro ao Subir Imagem"));
        }

        Optional<users_entity> user = _usersRepository.findById(file.getOwner());

        if(user.isEmpty()){
            return ResponseEntity.status(400).body(new AppResponse(
                    null,
                    true,
                    400,
                    "Usuário Não Encontrado"));
        }

        users_entity newUser = user.get();

        newUser.setProfile_image(result.fileURL());

        System.out.println(newUser.getProfile_image());

        _usersRepository.save(newUser);


        return ResponseEntity.status(200)
                .body(new AppResponse(
                        null,
                        false,
                        200,
                        "Upload com Sucesso!"));
    }

    public ResponseEntity<AppResponse> updatePassword(UpdatePasswordDto updatePasswordData){
        System.out.print(updatePasswordData.getCode());
        System.out.print( updatePasswordData.getEmail());
        System.out.print(updatePasswordData.getPassword());

        var recovery = _passwordRecoveryRepository.findRecoveryByRecoveryCodeAndIsActive(updatePasswordData.getCode(),
                true);

        if(recovery.isEmpty()){
            return ResponseEntity.status(400)
                    .body(new AppResponse(
                            null,
                            true,
                            400,
                            "Não Existe Recuperação Ativa Para esse Código"));
        }
        var user = _usersRepository.findById(recovery.get().getOwnerId());

        if(user.isEmpty()){
            return ResponseEntity.status(400)
                    .body(new AppResponse(null,
                            true,
                            400,
                            "Recuperação de Senha Invalida"));
        }

        if(user.get().verifyPassword(updatePasswordData.getPassword(), _passwordEncoder)){
            return ResponseEntity.status(400)
                    .body(new AppResponse(
                            null,
                            true,
                            400,
                            "A Nova Senha Deve ser Diferente da Anterior"
                    ));
        }

        var newUser = user.get();

        var newRecovery = recovery.get();

        newRecovery.setActive(false);
        newUser.setPassword(_passwordEncoder.encode(updatePasswordData.getPassword()));

        _usersRepository.save(newUser);
        _passwordRecoveryRepository.save(newRecovery);

        return ResponseEntity.status(200)
                .body(new AppResponse(
                        null,
                        false,
                        200,
                        "Senha Atualizada com Sucesso!"
                ));
    }
}
