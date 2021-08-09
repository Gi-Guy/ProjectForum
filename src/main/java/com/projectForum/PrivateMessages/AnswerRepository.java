package com.projectForum.PrivateMessages;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer>{

	Answer findById(int id);
	List<Answer> findByConversation(Conversation conversation);
}
