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

	public AccessDeniedRequestException() {
		super();
	}
	
	public void throwNewAccessDenied(String username, String location) {
		String message = "User: '" + username
		+ "' attempted to access the protected page: "
		+ location;
		throw new AccessDeniedRequestException(message);
	}

}
