package it.s3utils.exception;

/**
 * Exception thrown when a file is not found in S3.
*/
public class FileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6082215551727746794L;

	public FileNotFoundException(String key) {
        super("File not found: " + key);
    }
}
