package dev.ali.socialmediaapi.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Service
@Slf4j
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner presigner;
    @Value("${environment.R2_BUCKET_NAME}")
    private String bucketName;

    public S3Service(S3Client s3Client, S3Presigner presigner) {
        this.s3Client = s3Client;
        this.presigner = presigner;
    }


    public void uploadFile(String keyName, MultipartFile file) throws IOException {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).metadata(Map.of("Content-Type", "image/jpeg")).key(keyName).build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public String getImgUrl(String filename) {
        String url = "";
        try {
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofDays(7))
                    .getObjectRequest(builder -> builder
                            .bucket(bucketName)
                            .key(filename)
                            .build())
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toString();

        } catch (NoSuchKeyException e) {
            log.error("No image found for this user");
        }

        return url;
    }


}
