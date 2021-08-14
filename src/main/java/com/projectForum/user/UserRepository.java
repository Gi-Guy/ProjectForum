package com.projectForum.user;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	User findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUsername(@Param("username") String username);
	
	@Query("SELECT u FROM User u WHERE u.id = ?1")
	User findUserById(int id);
	
	List<User> findByOrderByRolesAsc();
	
	List<User> findAll();
	
}