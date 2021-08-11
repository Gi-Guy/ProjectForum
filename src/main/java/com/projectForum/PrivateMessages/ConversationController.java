package com.projectForum.PrivateMessages;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projectForum.Services.ConversationServices;
import com.projectForum.user.User;

@Controller
@RequestMapping("/messages/")
public class ConversationController {
	
	@Autowired
	private ConversationServices conversationServices;
	
	/**
	 * 	This method will display all messages of an user.*/
	//@RequestMapping("{username}")
	/*@GetMapping("{username}")
	public String displayAllUserMessages(@PathVariable String username, Authentication authentication,
												Model model) {
		User user = conversationServices.getUuserByUsername(username);
		
		if (user == null) {
			// TODO Handle this exception
			return null;
		}
		List<Conversation> convs = conversationServices.getAllConversationsByUser(user, authentication);
		
		if(convs.isEmpty() || convs == null)
			model.addAttribute("isEmpty", true);
		else
			model.addAttribute("convs",convs);
		
		return "messages";
	}*/
	@RequestMapping("")
	ModelAndView displayAllUserMessages(@RequestParam(name = "username") String username,Authentication authentication ) {
		ModelAndView mav = new ModelAndView("messages");
		User user = conversationServices.getUuserByUsername(username);
		
		if (user == null || !user.getUsername().equals(authentication.getName())) {
			// TODO Handle this exception
			mav = new ModelAndView("404");
			return mav;
		}
		List<Conversation> convs = conversationServices.getAllConversationsByUser(user, authentication);
		if(convs.isEmpty() || convs == null)
			mav.addObject("isEmpty", true);
		else
			mav.addObject("convs",convs);
		
		return mav;
	}
	/**
	 * 	This method will display a Conversation by conversationId
	 * */
	@GetMapping("id/{conversationId}")
	public String getConversationbyId(@PathVariable int conversationId, Model model, 
										Authentication authentication) {
		
		Conversation conv = conversationServices.findConversation(conversationId);
		List<Answer> answers = conversationServices.getAllAnswersInConversation(conversationId);
		if (conv == null) {
			// TODO direct to an error page	
		}
			
		// Checking if user is allowed to watch the conversation
		if ( conv.getSender().getUsername().equals(authentication.getName())||
				authentication.getName().equals(conv.getReceiver().getUsername())){
			// User allowed to watch the conversation.
			model.addAttribute("conversation",conv);
			model.addAttribute("answers",answers);
			model.addAttribute("newAnswer", new Answer());
			return "conversation";
		}
		 return "redirect:/messages/";
	}
	
	/**
	 * 	This method will update an exists conversation with new answer.*/
	@PostMapping("{conversationId}")
	public String addNewAnswer(@Valid @ModelAttribute Answer answer, @PathVariable int conversationId,
								Authentication authentication, Model model, BindingResult bindingResult) {
		// If hasErrors == true, then return to conversation page, because something went wrong
		if(bindingResult.hasErrors()) {
			System.err.println("ERROR :: Conversation Controller - addNewAnswer (POST)");
			model.addAttribute("conversation",conversationServices.findConversation(conversationId));
			model.addAttribute("newAnswer", new Answer());
			return "conversation";
		}
		
		// No errors, adding new answer
		conversationServices.addNewAnswer(conversationId, answer, authentication);
		model.asMap().clear();
		return "redirect:/messages/id/" + conversationId;
	}
	/**
	 * 	This method will create an Conversation between two users*/
	@GetMapping("newMessage/{receiverId}")
	public String createNewConversation(@PathVariable int receiverId,Authentication authentication,
												Model model) {
		Conversation newConv = conversationServices.createNewConversation(receiverId
							,conversationServices.getUuserByUsername(authentication.getName()));
		// Checking if user sending messages to itself
		if(newConv.getSender().equals(newConv.getReceiver())) {
			// User can't create a new conversation with itself!
			// TODO return error message
			return "redirect:/";
		}
		model.addAttribute("newConv",newConv);
		return "new_Conversation_page";
	}
	/**
	 * 	This method will proccess a new Conversation between two users*/
	@PostMapping("NewConversation")
	public String proccesNewConversation(@Valid @ModelAttribute Conversation conversation, 
					BindingResult bindingResult, Authentication authentication, Model model) {
		// If hasErrors == true, then return to Conversation page, because something went wrong
		if(bindingResult.hasErrors()) {
			System.err.println("ERROR :: Conversation Controller - proccesNewConversation (POST)");
			
			Conversation newConv = conversationServices.createNewConversation(conversation.getReceiver().getId()
					,conversationServices.getUuserByUsername(authentication.getName()));
			model.addAttribute("newConv",newConv);
			return "new_Conversation_page";
		}
		// Checking if message isn't blanked
		if(conversation.getTitle().isEmpty() || conversation.getContent().isBlank()) {
			Conversation newConv = conversationServices.createNewConversation(conversation.getReceiver().getId()
					,conversationServices.getUuserByUsername(authentication.getName()));
			model.addAttribute("newConv",newConv);
			return "new_Conversation_page";
		}
		
		// Message is legit
		return "redirect:/messages/" + conversationServices.proccessNewConversation(conversation, authentication).getId();
	}
	/**
	 * This method will delete an Answer*/
	@GetMapping("delete/answer/{answerId}")
	public String deleteAnswer(@PathVariable int answerId,Authentication authentication,
								RedirectAttributes model) {
		// find if answer is exists and user allowed to delete it
		Answer answer = conversationServices.getAnswer(answerId);
		Conversation conversation = answer.getConversation();
		
		// Checking everything. 
		if(answer == null || conversation == null || authentication == null ||
				!answer.getUser().getUsername().equals(authentication.getName())){
			// User not allowed to remove this
			return "redirect:/messages/" + conversation.getId();
		}
		// User allowed to remove answer
		conversationServices.deleteAnswer(answerId);
		model.addFlashAttribute("message", "Answer has been removed.");
		return "redirect:/messages/" + conversation.getId();
	}
	/**
	 * This method will delete an conversation*/
	@GetMapping("delete/conversation/{conversationId}")
	public String deleteConversation(@PathVariable int conversationId,Authentication authentication,
										RedirectAttributes model) {
		// Finding conversation
		Conversation conversation = conversationServices.getConversation(conversationId);
		
		if(	conversation == null || authentication == null ||
			!conversation.getSender().getUsername().equals(authentication.getName()) &&
			!conversation.getReceiver().getUsername().equals(authentication.getName())){
			// User not allowed to remove this
			return "redirect:/messages/" + conversation.getId();
		}
		//	User allowed to remove conversation
		conversationServices.deleteConversation(conversationId);
		model.addFlashAttribute("message", "Conversation has been removed.");
		return "redirect:/";
	}
}
