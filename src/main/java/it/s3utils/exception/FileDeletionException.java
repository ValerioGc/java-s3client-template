package it.s3utils.exception;

/**
 * Exception thrown when a file cannot be deleted from S3.
*/
public class FileDeletionException extends S3StorageException {

	private static final long serialVersionUID = 7473539424474954650L;

	public FileDeletionException(String key, Throwable cause) {
        super("Unable to delete file: " + key, cause);
    }
}
