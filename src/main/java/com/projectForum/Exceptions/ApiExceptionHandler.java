package com.projectForum.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ApiExceptionHandler {

	
	@ExceptionHandler(value = {EntityRequestException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String EntityException(EntityRequestException e){
		return "404";
	}
	
	@ExceptionHandler(value = {AccessDeniedRequestException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String AccessDenied(AccessDeniedRequestException e){
		return "405";
	}
	
}
