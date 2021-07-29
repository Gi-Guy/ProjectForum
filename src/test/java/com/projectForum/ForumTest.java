package com.projectForum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.Topic;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)//Defining to use the real DB
@Rollback(false)//We are commit to changes.
public class ForumTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private TopicRepository topicRepo;
	@Autowired
	private PostRepository postRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ForumRepository forumRepo;
	
	@Test
	public void testCreateForum() {
		String name = "Dummy Forum";
		String description = "This is a dummy forum for testing and etc. Nice work dude!";
		Forum forum = new Forum(name, description);
		
		forumRepo.save(forum);
	}
	@Test
	public void testAddTopicToForum() {
		Forum forum = forumRepo.findById(1);
		/* Testing forumRepo
		System.out.println("is it null? " + forum);
		System.out.println("what is the name? " + forum.getName());
		System.out.println("what is the description? " + forum.getDescription());
		*/
		Topic topic = topicRepo.findTopicById(22);
		topic.setForum(forum);
		topicRepo.save(topic);
	}
	
}
