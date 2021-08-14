package com.projectForum.Exceptions;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 	This controller will redirect users to errors pages
 * */
@Controller
public class ExceptionController implements ErrorController{
	
	
    @RequestMapping("/error")
    public String handleError() {
    	return "404";
    }
    
	@RequestMapping("/403")
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String AccessDenied() {
		return "403";
	}
	
	@RequestMapping("/404")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String MissingPage() {
		return "404";
	}
}
