package com.projectForum.Exceptions;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 	This controller will redirect users to errors pages
 * */
@Controller
public class ExceptionController {
	
	@RequestMapping("/405")
	public String AccessDenied() {
		return "405";
	}
	
	@RequestMapping("/404")
	public String MissingPage() {
		return "404";
	}
}
