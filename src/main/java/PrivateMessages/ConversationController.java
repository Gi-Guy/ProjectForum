package PrivateMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/messages/")
public class ConversationController {
	
	@Autowired
	private ConversationServices conversationServices;
	
	@GetMapping("{conversationId}")
	public String getConversationbyId() {
		return "";
	}
	public String addNewAnswer() {
		return "";
	}
	public String createNewConversation() {
		return "";
	}
	public String proccesNewConversation() {
		return "";
	}
	public String deleteAnswer() {
		return "";
	}
	public String deleteConversation() {
		return "";
	}
}
