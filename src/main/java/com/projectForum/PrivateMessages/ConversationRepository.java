package com.projectForum.PrivateMessages;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectForum.user.User;

public interface ConversationRepository extends JpaRepository<Conversation, Integer>{
	
	Conversation findById(int id);
	List<Conversation> findBySenderOrReceiver(User sender,User receiver);
	List<Conversation> findBySenderOrReceiverAndId(User sender,User receiver,int id);
	List<Conversation> findByLastActivityBefore(LocalDateTime localDateTime);
	List<Conversation> findBySenderOrReceiverOrderByLastActivityDesc(User sender,User receiver);

}
