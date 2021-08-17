package com.projectForum.Exceptions;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl{

	public static final Logger LOG
    = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			LOG.warn("User: '" + auth.getName()
					+ "' attempted to access the protected page: "
					+request.getRequestURI());
		}
		
		response.sendRedirect(request.getContextPath() + "/403");

	}
	
	
	
}
