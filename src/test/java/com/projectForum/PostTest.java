package com.projectForum;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.projectForum.post.Post;
import com.projectForum.post.PostRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)//Defining to use the real DB
@Rollback(true)//We are commit to changes.
public class PostTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private PostRepository postRepo;
	@Autowired
	private UserRepository userRepo;
	
	@Test
	public void testCreatePost() {
		User user = userRepo.findByEmail("Err@err.err");
		Post post = new Post();
		post.setContent("This is an expample test for a dummy post! Hello world!");
		post.setCreatedDate(LocalDateTime.now());
		post.setUser(user);
		
		Post savedPost = postRepo.save(post);
		Post existPost = entityManager.find(Post.class, savedPost.getId());
		assertThat(post.getId()).isEqualTo(post.getId());
		
	}
	
}
