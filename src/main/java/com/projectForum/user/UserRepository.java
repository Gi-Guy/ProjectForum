package com.projectForum.user;

import java.util.List;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	User findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.username = ?1")
	User findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.id = ?1")
	User findUserById(int id);
	
	List<User> findAll();
}