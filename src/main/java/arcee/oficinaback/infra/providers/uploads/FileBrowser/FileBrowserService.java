package arcee.oficinaback.infra.providers.uploads.FileBrowser;

import arcee.oficinaback.configs.FileBrowserConfig;
import arcee.oficinaback.users.dtos.UploadProfileImageDto;
import arcee.oficinaback.users.records.CreateFileResponse;
import arcee.oficinaback.users.records.UploadProfileImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class FileBrowserService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    FileBrowserConfig fileBrowserConfig;

    private String getToken() {
        String url = fileBrowserConfig.getLoginUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username",fileBrowserConfig.getUsername());
        requestBody.put("password", fileBrowserConfig.getPassword());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForObject(url, requestEntity, String.class);
    }

    public UploadProfileImageResponse UploadProfileImage(UploadProfileImageDto file){
        String token = getToken();

        String filename = file.getFilename();

        String path = fileBrowserConfig.getBaseUploadPath() + "/" +  filename.trim() + "?override=true";
        String publicPath = fileBrowserConfig.getShareUploadPublic() + "/" +  filename.trim();

        String createBaseFileResponse = this.CreateBaseFile(token,file.getFileType(), file.getData(),path);

        CreateFileResponse createPublicFileResponse = this.CreatePublicFile(token,publicPath);

        System.out.println(createPublicFileResponse);

        return new UploadProfileImageResponse(
                fileBrowserConfig.getSharedUrl()
                        .replace("@@HASH@@",createPublicFileResponse.hash()),
                false);
    }

    private String CreateBaseFile(String token, MediaType fileType, byte[] fileData, String path){
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth",token);
        headers.setContentType(fileType);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileData, headers);

        return restTemplate.postForObject(path,requestEntity, String.class );
    }

    private CreateFileResponse CreatePublicFile(String token, String publicPath){
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth",token);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("password", "");
        requestBody.put("expires", "820");
        requestBody.put("unit", "days");

        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(requestBody,headers);

        return restTemplate.postForObject(publicPath,requestEntity, CreateFileResponse.class);
    }

}
