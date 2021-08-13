package com.projectForum.login;



import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class LoginController {

  // Login form
  @RequestMapping("/login")
  public String login(Model model) {
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	  if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
		  return "login";
	  }
    return "redirect:/";
  }
 
  // Login form with error
  @RequestMapping("/loginError")
  public String loginError(Model model) {
    model.addAttribute("loginError", true);
    return "login";
  }

}