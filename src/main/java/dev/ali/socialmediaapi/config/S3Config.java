package dev.ali.socialmediaapi.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Getter
@Configuration
@Slf4j
public class S3Config {

    @Value("${environment.R2_ACCESS_KEY}")
    private String accessKey;
    @Value("${environment.R2_SECRET_KEY}")
    private String secretKey;
    @Value("${environment.R2_ACCOUNT_ID}")
    private String accountId;

    @Bean
    public S3Client buildS3Client(S3Config config) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(config.getAccessKey(), config.getSecretKey());
        S3Configuration serviceConfiguration = S3Configuration.builder().pathStyleAccessEnabled(true).build();


        return S3Client.builder()
                .endpointOverride(URI.create(String.format("https://%s.r2.cloudflarestorage.com", getAccountId())))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of("auto"))
                .serviceConfiguration(serviceConfiguration)
                .build();    }

    @Bean
    public S3Presigner buildS3Presigner(S3Config config) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                config.getAccessKey(),
                config.getSecretKey()
        );

        return S3Presigner.builder()
                .endpointOverride(URI.create(String.format("https://%s.r2.cloudflarestorage.com", getAccountId())))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of("auto"))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }



}
