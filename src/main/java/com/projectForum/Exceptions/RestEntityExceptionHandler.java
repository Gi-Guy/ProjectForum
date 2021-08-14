package com.projectForum.Exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestEntityExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleRestConflict(){
		System.err.println("hey?");
		return null;
	}
}
