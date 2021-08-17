package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.projectForum.PrivateMessages.Answer;
import com.projectForum.PrivateMessages.Conversation;
import com.projectForum.PrivateMessages.ConversationRepository;
import com.projectForum.user.User;

@Service
public class ConversationServices {
	
		private UserServices			userServices;
		private AnswerServices			answerServices;
		private	ConversationRepository 	convRepo;
		private DeleteService			deleteServices;
		
		@Autowired
		public ConversationServices(UserServices userServices, AnswerServices answerServices,
				ConversationRepository convRepo, DeleteService deleteServices) {
			super();
			this.userServices = userServices;
			this.answerServices = answerServices;
			this.convRepo = convRepo;
			this.deleteServices = deleteServices;
		}

		
		/**
		 * This method will return an User by username*/
		public User getUserByUsername(String username) {
			return userServices.findUserByUsername(username);
		}
		/**
		 * 	This method will return a list of all Conversation by User
		 * */
		public List<Conversation> getAllConversationsByUser(User user, Authentication authentication){
			List<Conversation> convs = null;
			if(user != null && user.getUsername().equals(authentication.getName()) || 
					userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN")) {
				
					convs = convRepo.findBySenderOrReceiver(user,user);	
			}
				
			return convs;
		}
		/**
		 * 	This method will return a list of all Conversation by User
		 * */
		public List<Conversation> getAllConversationsByUser(User user){
			List<Conversation> convs = null;
			if(user != null) {
				
					convs = convRepo.findBySenderOrReceiver(user,user);	
			}
				
			return convs;
		}
		/**
		 * 	This method will return an exists Conversation.
		 * */
		public Conversation findConversation(int convId) {
			return convRepo.findById(convId);
		}
		/**
		 * 	This method will add an answer to an exists conversatio*/
		public void addNewAnswer(int conversationId, Answer answer, Authentication authentication) {
			Conversation conv = convRepo.findById(conversationId);
			
			answer.setConversation(conv);
			answer.setUser(userServices.findUserByUsername(authentication.getName()));
			answerServices.save(answer);
			
		}
		
		/**
		 * 	This method will create an conversatio between two users.
		 * @param Int receiverId - the target user
		 * @param Int senderId - the sender*/
		public Conversation createNewConversation(int receiverId, User sender) {
			Conversation conversation = new Conversation();
			conversation.setReceiver(userServices.findUserByUserId(receiverId));
			conversation.setSender(sender);
			
			return conversation;
		}
		/**
		 * This method will proccess a new conversation between two users
		 * */
		public Conversation proccessNewConversation(Conversation conversation, Authentication authentication) {
			User receiver = userServices.findUserByUserId(conversation.getReceiver().getId());
			User sender = userServices.findUserByUsername(authentication.getName());
			
			conversation.setReceiver(receiver);
			conversation.setSender(sender);
			return convRepo.save(conversation);
		}
		
		/**
		 * 	This method will delete an Answer by id
		 * @param answerId*/
		public void deleteAnswer(int answerId) {
			deleteServices.deleteAnswer(answerId);
		}
		
		/**
		 * 	This method will delete an conversation by id.
		 * @param conversationId*/
		public void deleteConversation(int conversationId) {
			deleteServices.deleteConversation(conversationId);
		}
		
		/**
		 * 	This method will return an Answer object find by answerId.
		 *	@param int answerId
		 *	@return Answer*/
		public Answer getAnswer(int answerId) {
			return answerServices.findAnswerById(answerId);
		}
		/**
		 * 	This method will return an Conversation object find by conversationId.
		 *	@param int conversationId
		 *	@return Conversation*/
		public Conversation getConversation(int conversationId) {
			return convRepo.findById(conversationId);
		}
		/**
		 * This Method will return a list of answers by conversationId.
		 * @param Int conversationId
		 * @return List<Answer> answers*/
		public List<Answer> getAllAnswersInConversation(int conversationId){
			Conversation conv = convRepo.findById(conversationId);
			return answerServices.findAnswersByConversation(conv);
		}
		
		/*
		 *	 Repository services
		 * */
}
