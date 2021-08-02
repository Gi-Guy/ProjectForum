package com.projectForum.Security;


import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	//Role findByName(String name);
	Role findRoleByName(String name);
	Role findRoleById(int id);
}
