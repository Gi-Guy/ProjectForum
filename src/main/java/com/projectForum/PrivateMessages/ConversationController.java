package com.projectForum.PrivateMessages;

import java.nio.file.AccessDeniedException;
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

import com.projectForum.Exceptions.AccessDeniedRequestException;
import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.Services.ConversationServices;
import com.projectForum.user.User;

@Controller
@RequestMapping("/messages/")
public class ConversationController {
	
	@Autowired
	private ConversationServices conversationServices;
	
	private AccessDeniedRequestException accessDeniedRequestException = new AccessDeniedRequestException();
	private final String localUrl = "/messages/";
	
	/**
	 * This method will display all messages of a user.
	 * @throws AccessDeniedException 
	 */
	@RequestMapping("")
	ModelAndView displayAllUserMessages(@RequestParam(name = "username") String username,Authentication authentication ) throws AccessDeniedException {
		ModelAndView mav = new ModelAndView("messages");
		User user = conversationServices.getUserByUsername(username);
		
		// Checking if user isn't null
		if (user == null) {
			throw new EntityRequestException("Something went wrong, could not reload messages for :: '" + username+"'");

		}
		// Checking if user is allowed to access the page
		if(!user.getUsername().equals(authentication.getName()))
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + username);
		
		List<Conversation> convs = conversationServices.getAllConversationsByUserOrderByLastactivity(user);
		if(convs.isEmpty() || convs == null)
			mav.addObject("isEmpty", true);
		else
			mav.addObject("convs",convs);
		
		return mav;
	}
	
	/**
	 * 	This method will display a Conversation by its conversationId
	 */
	@GetMapping("id/{conversationId}")
	public String getConversationbyId(@PathVariable int conversationId, Model model, 
										Authentication authentication) {
		
		Conversation conv = conversationServices.findConversation(conversationId);
		List<Answer> answers = conversationServices.getAllAnswersInConversation(conversationId);
		if (conv == null) {
			throw new EntityRequestException("Could not reload conversation: '" + conversationId +"'");
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
		accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "id/" + conversationId);
		 return "redirect:/messages/";
	}
	
	/**
	 * 	This method will update an existing conversation with a new answer.
	 */
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
	 * 	This method will create a new Conversation between two users
	 */
	@GetMapping("newMessage/{receiverId}")
	public String createNewConversation(@PathVariable int receiverId ,Authentication authentication,
												Model model) {
		
		// Checking if receiver has reached to limit
		if(conversationServices.isReachedToLimit(receiverId)) {
			model.addAttribute("isSender", false);
			return "inboxFull";
		}
		// Checking if sender has reached to limit
		else if(conversationServices.isReachedToLimit(authentication.getName())) {
			model.addAttribute("isSender", true);
			return "inboxFull";
		}
		
		// User isn't in limit
		Conversation newConv = conversationServices.createNewConversation(receiverId
							,conversationServices.getUserByUsername(authentication.getName()));
		// Checking if user sending messages to itself
		if(newConv.getSender().equals(newConv.getReceiver())) {
			// User can't create a new conversation with itself!
			// TODO return error message
			return "redirect:" + localUrl;
		}
		model.addAttribute("newConv",newConv);
		return "new_Conversation_page";
	}
	
	/**
	 * 	This method will process a new Conversation between two users
	 */
	@PostMapping("NewConversation")
	public String proccesNewConversation(@Valid @ModelAttribute Conversation conversation, 
					BindingResult bindingResult, Authentication authentication, Model model) {
		// If hasErrors == true, then return to Conversation page, because something went wrong
		if(bindingResult.hasErrors()) {
			System.err.println("ERROR :: Conversation Controller - proccesNewConversation (POST)");
			
			Conversation newConv = conversationServices.createNewConversation(conversation.getReceiver().getId()
					,conversationServices.getUserByUsername(authentication.getName()));
			model.addAttribute("newConv",newConv);
			return "new_Conversation_page";
		}
		// Checking if message isn't blank
		if(conversation.getTitle().isEmpty() || conversation.getContent().isBlank()) {
			Conversation newConv = conversationServices.createNewConversation(conversation.getReceiver().getId()
					,conversationServices.getUserByUsername(authentication.getName()));
			model.addAttribute("newConv",newConv);
			return "new_Conversation_page";
		}
		
		// Message is valid
		return "redirect:/messages/id/" + conversationServices.proccessNewConversation(conversation, authentication).getId();
	}
	
	/**
	 * This method will delete an Answer
	 */
	@GetMapping("delete/answer/{answerId}")
	public String deleteAnswer(@PathVariable int answerId,Authentication authentication,
								RedirectAttributes model) {
		// find if answer exists and whether the user is allowed to delete it
		Answer answer = conversationServices.getAnswer(answerId);
		Conversation conversation = answer.getConversation();
		
		if(authentication == null)
			accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "delete/answer/" + answerId);
		// Checking everything. 
		if(answer == null || conversation == null ||
				!answer.getUser().getUsername().equals(authentication.getName())){
			// User not allowed to remove this
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "delete/answer/" + answerId);
			return "redirect:/messages/id/" + conversation.getId();
		}
		// User allowed to remove answer
		conversationServices.deleteAnswer(answerId);
		model.addFlashAttribute("message", "Answer has been removed.");
		return "redirect:/messages/id/" + conversation.getId();
	}
	
	/**
	 * This method will delete a conversation
	 * @throws AccessDeniedException 
	 */
	@GetMapping("delete/conversation/{conversationId}")
	public ModelAndView deleteConversation(@PathVariable int conversationId,Authentication authentication,
										RedirectAttributes model) throws AccessDeniedException {
		// Finding conversation
		Conversation conversation = conversationServices.getConversation(conversationId);
		
		if(authentication == null)
			accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "delete/conversation/" + conversationId);
		
		if(	conversation == null ||
			!conversation.getSender().getUsername().equals(authentication.getName()) &&
			!conversation.getReceiver().getUsername().equals(authentication.getName())){
			// User not allowed to remove this
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "delete/conversation/" + conversationId);
			return this.displayAllUserMessages(authentication.getName(), authentication);
		}
		//	User allowed to remove conversation
		conversationServices.deleteConversation(conversationId);
		model.addFlashAttribute("message", "Conversation has been removed.");
		return this.displayAllUserMessages(authentication.getName(), authentication);
	}
}
