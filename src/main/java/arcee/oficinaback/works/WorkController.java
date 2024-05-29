package arcee.oficinaback.works;


import arcee.oficinaback.configs.AppResponse;
import arcee.oficinaback.works.dtos.CreateWorkDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/works")
public class WorkController {

    @Autowired
    WorkServices _workServices;

    @PostMapping("/create")
    public ResponseEntity<AppResponse> createWork(@RequestBody CreateWorkDto workData, JwtAuthenticationToken token){
        return _workServices.createWork(workData,token.getName());
    }

    @GetMapping("/")
    public ResponseEntity<AppResponse> listWorks(JwtAuthenticationToken token){
        return _workServices.listWorks(token.getName());
    }

    @PostMapping("/delete")
    public ResponseEntity<AppResponse> deleteWork(@RequestBody JsonNode requestBody, JwtAuthenticationToken token){
        String workId = requestBody.get("workId").asText();

        return _workServices.deleteWork(workId,token.getName());
    }


}
