package com.projectForum.forum;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

/**
 * A fourm is a collection of topics
 * A forum has a title and a description only.
 * 
 * Each Topic had to be attached to a forum.
 * Each forum had {}* topics (0 or more)
 * 
 * A forums will keep a creation date in case admin will want to delete old forums.
 * 
 * The table will be called 'forums'*/

@Entity
@Table(name = "forums")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Forum {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(updatable = true, nullable = false, length = 50)
	private String name;
	
	@Column(updatable = true, nullable = true, length = 100, columnDefinition = "TEXT")
	private String description;
	
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdDate;
	
	@Column(updatable = true, nullable = false)
	private int priority;
	
	
	public Forum() {
		
	}
	public Forum(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	@PrePersist
	 protected void onCreate() {
		 this.createdDate = LocalDateTime.now();
	 }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id=id;
	}
	public int getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	
	public Forum getForum() {
		return this;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
    //Auto generated 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + priority;
		return result;
	}
    //Auto generated 
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Forum other = (Forum) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Forum [id=" + id + ", name=" + name + ", description=" + description + ", createdDate=" + createdDate
				+ ", priority=" + priority + "]";
	}
	
	
}
