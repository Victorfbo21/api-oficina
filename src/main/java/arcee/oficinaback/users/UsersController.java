package arcee.oficinaback.users;


import arcee.oficinaback.configs.AppResponse;
import arcee.oficinaback.users.dtos.CreateUserDto;
import arcee.oficinaback.users.dtos.UpdatePasswordDto;
import arcee.oficinaback.users.dtos.UploadProfileImageDto;
import arcee.oficinaback.users.records.UpdatePasswordRecord;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UsersService _usersService;

    @GetMapping("/list")
    public List<users_entity> getAllUsers(){
        return _usersService.getAllUsers();
    }

    @PostMapping("/create")
    public ResponseEntity<AppResponse> createUser(@RequestBody CreateUserDto user){
        return _usersService.createUser(user);
    }

    @PostMapping("/delete")
    public ResponseEntity<AppResponse> deleteUser(@RequestBody JsonNode requestBody, JwtAuthenticationToken token){
        String toDeleteId = requestBody.get("toDeleteId").asText();
        return _usersService.deleteUser(toDeleteId, token);
    }


    @PostMapping("/password-recovery")
    public ResponseEntity<AppResponse> sendPasswordRecovery(@RequestBody JsonNode requestBody ){
        String toSendEmail = requestBody.get("email").asText();

        return _usersService.sendPasswordRecovery(toSendEmail);
    }

    @PostMapping("/upload")
    public ResponseEntity<AppResponse> uploadProfileImage(@RequestParam("file") MultipartFile file, JwtAuthenticationToken token)  {
        UploadProfileImageDto finalFile = new UploadProfileImageDto();
        try {
            finalFile.setData(file.getBytes());
            finalFile.setFilename(file.getOriginalFilename());
            finalFile.setFileType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            finalFile.setFileSize(file.getSize());
            finalFile.setOwner(token.getName());

            return _usersService.uploadProfileImage(finalFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<AppResponse> updatePassword(@RequestBody UpdatePasswordDto updatePasswordData){
        return _usersService.updatePassword(updatePasswordData);
    }


}
