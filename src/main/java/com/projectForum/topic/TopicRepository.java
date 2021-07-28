package com.projectForum.topic;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.projectForum.user.User;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
	
	@Transactional
	void deleteTopicById(int id);
	
	Set<Topic> findTopicsByUser(User user);
	
	@Query("SELECT t FROM Topic t WHERE t.id = ?1")
	Topic findTopicById(int id);
}