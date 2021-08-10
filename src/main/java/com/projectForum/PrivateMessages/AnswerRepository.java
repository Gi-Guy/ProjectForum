package com.projectForum.PrivateMessages;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectForum.user.User;

public interface AnswerRepository extends JpaRepository<Answer, Integer>{

	Answer findById(int id);
	List<Answer> findByUser(User user);
	List<Answer> findByConversation(Conversation conversation);
	List<Answer> findByConversationAndUser(Conversation conversation, User user);
	
}
