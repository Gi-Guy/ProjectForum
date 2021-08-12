package com.projectForum.Exceptions;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ApiExceptionHandler {

	
	@ExceptionHandler(value = {ApiRequestException.class})
	public ResponseEntity<Object> handleApiRequwstException(ApiRequestException e){
		HttpStatus	badRequest = HttpStatus.BAD_REQUEST;
		//	Create payload containing exception details
		ApiException apiException = new ApiException(e.getMessage(),
													 e,
													 badRequest,
													 ZonedDateTime.now());
		//	Return ResponseEntity
		return new ResponseEntity<>(apiException,badRequest);
	}
	
	@ExceptionHandler(value = {EntityRequestException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String EntityException(EntityRequestException e){
		return "404";
	}
	
	
}