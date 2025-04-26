package it.s3utils.client;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Utility interface for performing basic S3 operations.
 */
public interface S3ClientUtil {

    /**
     * Initializes and returns the S3 client configured with credentials and region.
     *
     * @return an initialized {@link S3Client} instance
    */
    S3Client getS3Client();

    /**
     * Returns a summary of the current S3 configuration (region, bucket name, keys).
     *
     * @return configuration info as a formatted {@link String}
    */
    String checkInfo();

    /**
     * Uploads a file to the S3 bucket under the given key.
     *
     * @param key - the object key (including path) where the file will be stored
     * @param file - the multipart file to upload
     * @throws FileUploadExceptionn if an error occurs reading the file bytes
    */
    void uploadFile(String key, MultipartFile file) throws IOException;

    /**
     * Replaces an existing file in the S3 bucket at the given key.
     *
     * @param key - the object key (including path) of the file to replace
     * @param file - the new multipart file content
     * @throws IOException if an error occurs reading the file bytes
    */
    void replaceFile(String key, MultipartFile file) throws IOException;
    
    /**
     * Download an existing file in the S3 bucket at the given key.
     *
     * @param key - the object key (including path) of the file to download
     * @param file - the new multipart file content
     * @throws FileNotFoundException if an error occurs reading the file bytes
    */
    byte[] downloadFile(String key) throws IOException;
    
    /**
     * Delete an existing file in the S3 bucket at the given key.
     *
     * @param key - the object key (including path) of the file to delete
     * @throws FileDeletionException if an error occurs deleting the file
    */
    void deleteFile(String key);

}