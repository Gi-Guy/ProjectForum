package com.projectForum.forum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, Integer> {
	
	List<Forum> findAll();
	List<Forum> findByOrderByPriorityAsc();
	
	Forum findById(int i);
	Forum findByPriority(int priority);
	
}
