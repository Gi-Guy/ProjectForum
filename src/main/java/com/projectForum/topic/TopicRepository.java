package com.projectForum.topic;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectForum.user.User;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
	Set<Topic> findByUser(User user);
}
