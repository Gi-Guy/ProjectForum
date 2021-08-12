package com.projectForum.Exceptions;

public class AccessDeniedRequestException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2886266886240024332L;

	public AccessDeniedRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessDeniedRequestException(String message) {
		super(message);
	}

	
}
