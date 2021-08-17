package com.projectForum;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.projectForum.PrivateMessages.Conversation;
import com.projectForum.PrivateMessages.ConversationRepository;
import com.projectForum.Security.RoleRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Using the real DB
@Rollback(true) // Committing the changes TODO make sure tests don't commit changes
public class conversationTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private ConversationRepository	convRepo;
	
	@Test
	public void testDisplayUserMessages() {
		// This method will display ALL conversations that user has as a sender or a receiver
		User user = userRepo.findUserById(1);
		List<Conversation> conversations = convRepo.findBySenderOrReceiver(user, user);
		for(int i=0; i<conversations.size(); i++) {
			System.err.println(conversations.get(i).toString());
		}
		
	}
	@Test
	public void testDisplayUserMessagesById() {
		// This method will display all conversations that user has as a sender or a receiver by conversation id
		User user = userRepo.findUserById(1);
		List<Conversation> conversations = convRepo.findBySenderOrReceiverAndId(user, user, 1);
		for(int i=0; i<conversations.size(); i++) {
			System.err.println(conversations.get(i).toString());
		}
		
	}
	
}
