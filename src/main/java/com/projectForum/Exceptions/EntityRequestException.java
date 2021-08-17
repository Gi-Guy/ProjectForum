package com.projectForum.Exceptions;

public class EntityRequestException extends RuntimeException{

	private static final long serialVersionUID = 4215445944835240741L;

	public EntityRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityRequestException(String message) {
		super(message);
	}
}
