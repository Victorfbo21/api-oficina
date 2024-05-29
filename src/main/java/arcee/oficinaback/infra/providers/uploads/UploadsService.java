package arcee.oficinaback.infra.providers.uploads;

import arcee.oficinaback.configs.FileBrowserConfig;
import arcee.oficinaback.infra.providers.uploads.FileBrowser.FileBrowserService;
import arcee.oficinaback.infra.providers.uploads.S3.S3Service;
import arcee.oficinaback.users.dtos.UploadProfileImageDto;
import arcee.oficinaback.users.records.CreateFileResponse;
import arcee.oficinaback.users.records.UploadProfileImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service

public class UploadsService {


    @Autowired
    FileBrowserService fileBrowserService;

    @Autowired
    S3Service s3Service;



    public UploadProfileImageResponse uploadProfileImage(UploadProfileImageDto file) throws IOException {
        var uploadFileBrowser = fileBrowserService.UploadProfileImage(file);

        if(uploadFileBrowser.error()){
            var uploadS3 = s3Service.uploadProfileImage(file);
            if(uploadS3.error()){
                return new UploadProfileImageResponse("", true);
            }
            return new UploadProfileImageResponse("", false);
        }

        return new UploadProfileImageResponse(uploadFileBrowser.fileURL(), false);
    }

}
