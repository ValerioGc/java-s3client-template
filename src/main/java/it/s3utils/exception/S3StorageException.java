package it.s3utils.exception;

/**
 * Exception thrown when an I/O error occurs during S3 storage operations.
 */
public class S3StorageException extends RuntimeException {
	
	private static final long serialVersionUID = 3202019843467387616L;
	
	public S3StorageException(String message) {
        super(message);
    }
	
    public S3StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
