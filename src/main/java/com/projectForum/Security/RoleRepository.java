package com.projectForum.Security;


import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Role findRoleByName(String name);
	Role findRoleById(int id);
}
