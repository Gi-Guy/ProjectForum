package com.projectForum.post;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projectForum.user.User;

public interface PostRepository extends JpaRepository<Post, Integer> {
	
	Set<Post> findByUser(User user);
	
	
	@Query("SELECT i FROM Post i WHERE i.id=?1")
	Post findById(int id);
}
