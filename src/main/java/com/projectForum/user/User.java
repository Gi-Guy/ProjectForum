package com.projectForum.user;

//import java.util.Collection;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectForum.Security.Role;


@Entity
@Table(name =  "user")
public class User {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column (unique = true, nullable = false, length = 15)
	private String username;
	
	@Column (unique = true, nullable = false, length = 64)
	private String email;
	
	@Column (name="First_name", nullable = false, length = 15)
	private String firstName;
	
	@Column (name="Last_name", nullable = false, length = 15)
	private String lastName;
	
	@Column(nullable = false, length = 60)
	private String password;
	
	@Column(nullable = false, length = 4)
	private boolean isActive = true;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("role_id ASC")
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	@JsonManagedReference
	private Set<Role> roles = new HashSet<>();
	
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void removeRole() {
		this.roles.removeAll(roles);
	}
	
	public void setRole(Role role) {
		this.roles.add(role);
	}

	public Role getRole() {
		return this.roles.iterator().next();
	}
	private LocalDateTime joiningDate;
	private LocalDateTime lastLogin;
	
	public boolean isActive() {
		return isActive;
	}
	
	public void SetInactive() {
		setActive(false);
	}
	
	public void SetActive() {
		setActive(true);
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
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
	
	
	public LocalDateTime getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@PrePersist
	 protected void onCreate() {
		 this.joiningDate = LocalDateTime.now();
		 this.lastLogin = LocalDateTime.now();
	 }
	
	 @PreUpdate
	protected void onUpdate() {
		this.lastLogin = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", password=" + password + ", isActive=" + isActive + ", role=" + this.getRole().getName()
				+ ", joiningDate=" + joiningDate + ", lastLogin=" + lastLogin + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result + (isActive ? 1231 : 1237);
		result = prime * result + ((joiningDate == null) ? 0 : joiningDate.hashCode());
		result = prime * result + ((lastLogin == null) ? 0 : lastLogin.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (isActive != other.isActive)
			return false;
		if (joiningDate == null) {
			if (other.joiningDate != null)
				return false;
		} else if (!joiningDate.equals(other.joiningDate))
			return false;
		if (lastLogin == null) {
			if (other.lastLogin != null)
				return false;
		} else if (!lastLogin.equals(other.lastLogin))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!getRole().getName().equals(other.getRole().getName()))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}