package arcee.oficinaback.infra.providers.uploads.S3;

import arcee.oficinaback.configs.S3Config;
import arcee.oficinaback.users.dtos.UploadProfileImageDto;
import arcee.oficinaback.users.records.UploadProfileImageResponse;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.model.Put;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    private final S3Config s3Config;
    public S3Service(S3Config s3Config){
        this.s3Config = s3Config;
        AWSCredentials credentials = new BasicAWSCredentials(s3Config.getAccessKeyId(),s3Config.getSecretAccessKey());

        this.amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-1")
                .build();
    }

    public UploadProfileImageResponse uploadProfileImage(UploadProfileImageDto file) throws IOException {

        File convFile = new File(file.getFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getData());
        fos.close();

        PutObjectRequest request = new PutObjectRequest(s3Config.getBucket(),file.getFilename(), convFile);

        var upload = this.amazonS3.putObject(request);

        return new UploadProfileImageResponse("Sucesso!",false);
    }
}
