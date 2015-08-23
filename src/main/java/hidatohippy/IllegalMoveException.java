package hidatohippy;

public class IllegalMoveException extends Exception {

	private static final long serialVersionUID = 8875433488740435144L;

	public IllegalMoveException() {
		super();
	}

	public IllegalMoveException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalMoveException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalMoveException(String message) {
		super(message);
	}

	public IllegalMoveException(Throwable cause) {
		super(cause);
	}

}
