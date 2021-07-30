package com.projectForum.forum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectForum.topic.Topic;


public interface ForumRepository extends JpaRepository<Forum, Integer> {
	
	List<Forum> findAll();
	
	Forum findById(int i);
	
}
