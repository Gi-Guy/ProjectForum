package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.PrivateMessages.Answer;
import com.projectForum.PrivateMessages.AnswerRepository;
import com.projectForum.PrivateMessages.Conversation;
import com.projectForum.user.User;

/**
 * 	This class will gives services for most post's actions
 * */

@Service
public class AnswerServices {

	@Autowired
	private AnswerRepository answerRepo;
	
	public Answer findAnswerById(int id) throws EntityRequestException{
		Answer answer = null;
		
		try {
			answer = answerRepo.findById(id);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find a answer by id :: " + id);
		}
		return answer;
	}
	
	public List<Answer> findAnswersByUser(User user) throws EntityRequestException{
		List<Answer> answers = null;
		
		try {
			answers = answerRepo.findByUser(user);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find a answers by Userid, username :: " + user.getId()
																						+" " + user.getUsername());
		}
		
		return answers;
	}
	public List<Answer> findAnswersByConversation(Conversation conversation) throws EntityRequestException{
		List<Answer> answers = null;
		
		try {
			answers = answerRepo.findByConversation(conversation);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find a answers by Conversation id :: " + conversation.getId());
		}
		
		return answers;
	}
	public List<Answer> findByConversationAndUser(Conversation conversation, User user) throws EntityRequestException{
		List<Answer> answers = null;
		
		try {
			answers = answerRepo.findByConversationAndUser(conversation, user);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find a answers by Conversation id & User id :: " + conversation.getId()
															+ " " + user.getId() + " " + user.getUsername());
		}
		
		return answers;
	}
	
	public void save(Answer answer) throws EntityRequestException{
		try {
			answerRepo.save(answer);
		} catch (Exception e) {
			throw new EntityRequestException("Could not save new answer in Conversation id :: " + answer.getConversation().getId());
		}
	}
	public void delete (Answer answer) throws EntityRequestException{
		try {
			answerRepo.delete(answer);
		} catch (Exception e) {
			throw new EntityRequestException("Could not delete answer in Conversation id :: " + answer.getConversation().getId());
		}
	}
}
