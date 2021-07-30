package com.projectForum.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.projectForum.topic.Topic;
import com.projectForum.user.User;

public interface PostRepository extends JpaRepository<Post, Integer> {
	
	Post findById(int i);
	@Transactional
	void deletePostById(int id);
	
	List<Post> findPostsByTopicId(int topicId);
	List<Post> findPostsByTopic(Topic topic);
	List<Post> findPostsByUser(User user);
}
