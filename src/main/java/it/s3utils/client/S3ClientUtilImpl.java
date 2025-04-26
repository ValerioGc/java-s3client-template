package it.s3utils.client;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.s3utils.exception.FileDeletionException;
import it.s3utils.exception.FileNotFoundException;
import it.s3utils.exception.FileUploadException;
import it.s3utils.exception.S3StorageException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.utils.StringUtils;

@Service
public class S3ClientUtilImpl implements S3ClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(S3ClientUtilImpl.class);

    @Value("${cloud.aws.s3.region}")
    private String region;

    @Value("${cloud.aws.s3.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.credentials.app-key}")
    private String appKey;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    private S3Client s3Client;

    @Override
    public S3Client getS3Client() {
        return Optional.ofNullable(s3Client)
            .orElseGet(() -> {
                s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(appKey, secretKey)))
                    .build();
                return s3Client;
            });
    }

    @Override
    public String checkInfo() {
        return String.format("Region: %s | Bucket: %s | SecretKey: %s | AppKey: %s",
                region, bucketName, secretKey, appKey);
    }

    @Override
    public void uploadFile(String key, MultipartFile file) throws IOException {
        try {
        	if(StringUtils.isBlank(key))
        		throw new FileDeletionException("Key cannot be null or empty", null);
        	
            PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            getS3Client().putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
            logger.info("File uploaded to S3 with key: {}", key);
        } catch (IOException e) {
            throw e;
        } catch (SdkException e) {
            throw new FileUploadException(key, e);
        }
    }

    @Override
    public void replaceFile(String key, MultipartFile file) throws IOException {
        try {
        	if(StringUtils.isBlank(key))
        		throw new FileDeletionException("Key cannot be null or empty", null);
        	
			if (file.isEmpty() || StringUtils.isBlank(file.getOriginalFilename()) || file.getSize() == 0)
				throw new FileUploadException(key, new IllegalArgumentException("File is not valid"));
			
            PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            getS3Client().putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
            logger.info("File replaced in S3 with key: {}", key);
        } catch (IOException e) {
            throw e;
        } catch (SdkException e) {
            throw new FileUploadException(key, e);
        }
    }

    @Override
    public byte[] downloadFile(String key) throws FileNotFoundException {
        try {
        	if(StringUtils.isBlank(key))
        		throw new FileDeletionException("Key cannot be null or empty", null);
        	
            GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            return getS3Client().getObject(getRequest).readAllBytes();
        } catch (NoSuchKeyException e) {
            throw new FileNotFoundException(key);
        } catch (IOException | SdkException e) {
            throw new S3StorageException("Failed to download file: " + key, e);
        }
    }

    @Override
    public void deleteFile(String key) {
        try {
        	if(StringUtils.isBlank(key))
        		throw new FileDeletionException("Key cannot be null or empty", null);
        		
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            getS3Client().deleteObject(deleteRequest);
            logger.info("File deleted from S3 with key: {}", key);
        } catch (SdkException e) {
            throw new FileDeletionException(key, e);
        }
    }
}
