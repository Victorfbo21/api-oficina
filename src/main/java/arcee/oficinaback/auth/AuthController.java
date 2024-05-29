package arcee.oficinaback.auth;


import arcee.oficinaback.auth.dtos.LoginRequest;
import arcee.oficinaback.auth.dtos.LoginResponse;
import arcee.oficinaback.configs.AppResponse;
import arcee.oficinaback.users.UsersRepository;
import arcee.oficinaback.users.users_entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private final UsersRepository _usersRepository;
    private final BCryptPasswordEncoder _bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthController(UsersRepository usersRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          JwtEncoder jwtEncoder)
    {
        this._usersRepository = usersRepository;
        this._bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AppResponse> login(@RequestBody LoginRequest loginRequest){

        var user = _usersRepository.findByEmail(loginRequest.email());

        if(user.isEmpty()){
            return ResponseEntity.status(400).body(new AppResponse(
                    null,
                    true,
                    400,
                    "Usuário Não Encontrado!"
            ));
        }

        if(!user.get().isLoginCorrect(loginRequest,_bCryptPasswordEncoder)){
            return ResponseEntity.status(400).body(new AppResponse(
                    null,
                    true,
                    400,
                    "Senha Invalida"
            ));
        }

        var jwtValue = generateJWTToken(user.get());

        var toReturnUser = createLoginResponse(user.get());

        return ResponseEntity
                .status(200)
                .body(new AppResponse(
                        new LoginResponse(jwtValue, 300L , toReturnUser),
                        false,
                        200,
                        "Logado com Sucesso!"));
    }

    private String generateJWTToken(users_entity user){
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("Backend Oficina")
                .subject(user.getId())
                .expiresAt(now.plusSeconds(300L))
                .issuedAt(now)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    private LoginUserResponse createLoginResponse(users_entity user){
        return new LoginUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfile_image(),
                user.getContact_number()
        );
    }
}