package lk.devfactory.exception;

public class RepositoryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RepositoryNotFoundException() {
		super();
	}

	public RepositoryNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RepositoryNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoryNotFoundException(String message) {
		super(message);
	}

	public RepositoryNotFoundException(Throwable cause) {
		super(cause);
	}

}
