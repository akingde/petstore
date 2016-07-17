package com.feiyangedu.petstore.context;

public class MissingContextException extends RuntimeException {

	public MissingContextException() {
		super();
	}

	public MissingContextException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingContextException(String message) {
		super(message);
	}

	public MissingContextException(Throwable cause) {
		super(cause);
	}

}
