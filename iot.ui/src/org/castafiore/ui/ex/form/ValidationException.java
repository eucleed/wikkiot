package org.castafiore.ui.ex.form;

import org.castafiore.ui.UIException;

public class ValidationException extends UIException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

	
}
