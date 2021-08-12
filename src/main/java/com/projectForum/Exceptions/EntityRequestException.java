package com.projectForum.Exceptions;

public class EntityRequestException extends RuntimeException{

	public EntityRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityRequestException(String message) {
		super(message);
	}

	
	
}
