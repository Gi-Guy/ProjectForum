package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.Security.Role;
import com.projectForum.Security.RoleRepository;

/**
 * This class will gives services for all Role's actions*/

@Service
public class RoleServices {

	@Autowired
	private RoleRepository	roleRepo;
	
	/**
	 * 	This method will find a Role by Role's Name
	 * @param String name
	 * @return Role*/
	public Role findRoleByName(String name) throws EntityRequestException{
		try {
			return roleRepo.findRoleByName(name);
		} catch (Exception e) {
			throw new EntityRequestException("Could not reload role by name :: " + name);
		}
	}
	
	/**
	 * 	This method will find a Role by Role's id
	 * @param Int roleId
	 * @return Role*/
	public Role findRoleById(int roleId) throws EntityRequestException{
		try {
			return roleRepo.findRoleById(roleId);
		} catch (Exception e) {
			throw new EntityRequestException("Could not reload role by id :: " + roleId);
		}
	}
	
	public List<Role> findAll() throws EntityRequestException{
		try {
			return roleRepo.findAll();
		} catch (Exception e) {
			throw new EntityRequestException("Could not reload all roles.");
		}
	}
	public Role save(Role role) throws EntityRequestException{
		try {
			return roleRepo.save(role);
		} catch (Exception e) {
			throw new EntityRequestException("Could not save new Role :: " + role.getName());
		}
	}
	
	public void delete(Role role) throws EntityRequestException{
		try {
			roleRepo.delete(role);
		} catch (Exception e) {
			throw new EntityRequestException("Could not delete :: " + role.getName());
		}
	}
	
	/*
	 * Actions*/
	/**
	 * 	This method will create a set of roles by List<String>*/
	public void setNewRoles(List<String> roles) throws EntityRequestException{
		//	Checking if there are new roles to add
		if(roles.isEmpty())
			return;
		
		for(String role : roles) {
			Role newRole = new Role(role); 
			this.save(newRole);
		}
	}
}
