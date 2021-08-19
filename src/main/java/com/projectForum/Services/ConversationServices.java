package com.projectForum.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.projectForum.Exceptions.EntityRequestException;
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
			this.userServices = userServices;
			this.answerServices = answerServices;
			this.convRepo = convRepo;
			this.deleteServices = deleteServices;
		}

		
		/**
		 * This method will return a User by username
		 */
		public User getUserByUsername(String username) {
			return userServices.findUserByUsername(username);
		}
		
		/**
		 * 	This method will return a list of all Conversations of a User
		 */
		public List<Conversation> getAllConversationsByUser(User user, Authentication authentication){
			List<Conversation> convs = null;
			if(user != null && user.getUsername().equals(authentication.getName()) || 
					userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN")) {
				
					convs = convRepo.findBySenderOrReceiver(user,user);	
			}
				
			return convs;
		}
		
		/**
		 * 	This method will return an existing Conversation.
		 */
		public Conversation findConversation(int convId) {
			return convRepo.findById(convId);
		}
		
		/**
		 * 	This method will add an answer to an existing conversation
		 */
		public void addNewAnswer(int conversationId, Answer answer, Authentication authentication) {
			Conversation conv = convRepo.findById(conversationId);
			conv.setLastActivity(LocalDateTime.now());
			answer.setConversation(conv);
			answer.setUser(userServices.findUserByUsername(authentication.getName()));
			answerServices.save(answer);
		}
		
		/**
		 * This method will create a conversation between two users.
		 * @param Int receiverId - the target user
		 * @param Int senderId - the sender
		 */
		public Conversation createNewConversation(int receiverId, User sender) {
			Conversation conversation = new Conversation();
			conversation.setReceiver(userServices.findUserByUserId(receiverId));
			conversation.setSender(sender);
			
			return conversation;
		}
		
		/**
		 * This method will process a new conversation between two users
		 */
		public Conversation proccessNewConversation(Conversation conversation, Authentication authentication) {
			User receiver = userServices.findUserByUserId(conversation.getReceiver().getId());
			User sender = userServices.findUserByUsername(authentication.getName());
			
			conversation.setReceiver(receiver);
			conversation.setSender(sender);
			return convRepo.save(conversation);
		}
		
		/**
		 * This method will delete an Answer by id
		 * @param answerId
		 */
		public void deleteAnswer(int answerId) {
			deleteServices.deleteAnswer(answerId);
		}
		
		/**
		 * This method will delete an conversation by id.
		 * @param conversationId
		 */
		public void deleteConversation(int conversationId) {
			deleteServices.deleteConversation(conversationId);
		}
		
		/**
		 * 	This method will return an Answer object with an answerId.
		 *	@param int answerId
		 *	@return Answer
		 */
		public Answer getAnswer(int answerId) {
			return answerServices.findAnswerById(answerId);
		}
		
		/**
		 * 	This method will return a Conversation object with a conversationId.
		 *	@param int conversationId
		 *	@return Conversation
		 */
		public Conversation getConversation(int conversationId) {
			return convRepo.findById(conversationId);
		}
		
		/**
		 * This Method will return a list of answers through their conversationId.
		 * @param Int conversationId
		 * @return List<Answer> answers
		 */
		public List<Answer> getAllAnswersInConversation(int conversationId){
			Conversation conv = convRepo.findById(conversationId);
			return answerServices.findAnswersByConversation(conv);
		}
		
		/**
		 * 	This method will return a list of conversations of User,
		 * 	Order by last activity date.
		 * @param User to search*/
		public List<Conversation> getAllConversationsByUserOrderByLastactivity(User user) throws EntityRequestException{
			try {
				return convRepo.findBySenderOrReceiverOrderByLastActivityDesc(user, user);
			} catch (Exception e) {
				throw new EntityRequestException("Could not reload user's conversations :: " + user.getUsername());
			}
		}
}
