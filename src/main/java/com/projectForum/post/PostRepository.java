package com.projectForum.post;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectForum.user.User;

public interface PostRepository extends JpaRepository<Post, Integer> {
	
	Set<Post> findByUser(User user);

}
