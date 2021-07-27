package com.projectForum.user;

import java.sql.Date;
//import java.util.Collection;
import java.time.LocalDateTime;

//import javax.management.relation.Role; //TODO: SOLVE THIS
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name =  "user")
//public class User implements UserDetails{
public class User {
	


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column (unique = true, nullable = false, length = 10)
	private String username;
	
	@Column (unique = true, nullable = false, length = 64)
	private String email;
	
	@Column (name="First_name" , nullable = false , length = 15)
	private String firstName;
	
	@Column (name="Last_name" ,nullable = false,  length = 15)
	private String lastName;
	
	@Column(nullable = false, length = 60)
	private String password;
	
	@Column(nullable = false, length = 4)
	private boolean isActive = true;
	
	private LocalDateTime joiningDate;
	private LocalDateTime lastLogin;
	
	//TODO: ADD ROLE OPTION - FOR ADMINS
	//TODO: Add forum messages list
	//TODO: Add topics list
	//TODO: Add private messages list
	
	
	public boolean isActive() {
		return isActive;
	}
	
	public void SetInactive() {
		this.isActive = false;
	}
	
	public void SetActive() {
		this.isActive = true;
	}

	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public LocalDateTime getJoiningDate() {
		return joiningDate;
	}
	
	public void setJoiningDate(LocalDateTime joiningDate) {
		this.joiningDate = joiningDate;
		this.setLastLogin(joiningDate);
	}
	
	/** Return when last time user been login.*/
	public LocalDateTime getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	

	
	
}
