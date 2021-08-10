package com.projectForum.PrivateMessages;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectForum.user.User;

public interface ConversationRepository extends JpaRepository<Conversation, Integer>{
	
	Conversation findById(int id);
	List<Conversation> findBySenderOrReceiver(User sender,User receiver);
	List<Conversation> findBySenderOrReceiverAndId(User sender,User receiver,int id);

}
