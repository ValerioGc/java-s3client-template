package it.s3utils.exception;

/**
 * Exception thrown when a file cannot be uploaded to S3.
*/
public class FileUploadException extends S3StorageException {

	private static final long serialVersionUID = 1875357299241285399L;

	public FileUploadException(String key, Throwable cause) {
        super("Unable to upload file: " + key, cause);
    }
}
