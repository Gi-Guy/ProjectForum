package com.projectForum;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.Post;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.Topic;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)//Defining to use the real DB
@Rollback(false)//We are commit to changes.
public class TopicTest {

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
	public void testCreateTopic() {
		User user = userRepo.findByEmail("Err@err.err");
		Post post = postRepo.findById(1);
		Topic topic = new Topic();
		
		topic.setUser(user);
		topic.setTitle("Test Dummy!");
		topic.setViews(7);
		topic.setContent("This is a test dummy for a new topic! This topic has no posts yet.");
		
		Topic savedTopic = topicRepo.save(topic);
		Topic existTopic = entityManager.find(Topic.class, savedTopic.getId());
		assertThat(topic.getId()).isEqualTo(existTopic.getId());
	}
	@Test 
	public void createNewTopicInForum() {
		User user = userRepo.findByEmail("Err@err.err");
		Post post = postRepo.findById(1);
		Forum forum = forumRepo.findById(1);
		Topic topic = new Topic();
		
		topic.setUser(user);
		topic.setTitle("Hello World!");
		topic.setContent("This is a test dummy for a new topic in a forum!");
		topic.setViews(0);
		topic.setForum(forum);
		
		topicRepo.save(topic);
	}
	@Test
	public void addPostToTopic() {
		User user = userRepo.findByEmail("Err@err.err");
		Topic topic = topicRepo.findTopicById(18);
		Post post = new Post();
		
		//Add stuff to new post
		post.setContent("Let's play a game!");
		post.setCreatedDate(LocalDateTime.now());
		post.setUser(user);
		post.setTopic(topic);
		postRepo.save(post);
		
	//	topic.setPosts(postRepo.findPostsByTopic(topic));
		
		//Topic savedTopic = topicRepo.save(topic);
		//Topic existTopic = entityManager.find(Topic.class,topic.getId());
		//assertThat(user.getId()).isEqualTo(topic.getId());
	}
	@Test
	public void delete() {
		
		List<Post> posts = postRepo.findPostsByTopic(topicRepo.findTopicById(18));
		for(int i=0; i<posts.size(); i++) {
			System.err.println("deleting post id:  " + posts.get(i).getId());
			postRepo.delete(posts.get(i));
		}
		topicRepo.delete(topicRepo.findTopicById(18));
	}
}
