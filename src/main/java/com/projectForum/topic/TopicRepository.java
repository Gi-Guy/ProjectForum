package com.projectForum.topic;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.projectForum.forum.Forum;
import com.projectForum.user.User;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
	
	@Transactional
	void deleteTopicById(int id);
	
	List<Topic> findTopicsByUser(User user);
	List<Topic> findTopicsByForumId(int forumId);
	List<Topic> findTopicsByForum(Forum forum);
	Topic findTopicById(int id);
}