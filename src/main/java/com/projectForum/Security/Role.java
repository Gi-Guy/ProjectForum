package com.projectForum.Security;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "roles")
public class Role {

	@Id
	@Column(name = "role_Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;    
    
	public Role(String name) {
		this.name = name;
	}
	
	public Role () {
		
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return this.name;
	}

	public int getId() {
		return id;
	}
	public Role getRole() {
		return this;
	}
}
