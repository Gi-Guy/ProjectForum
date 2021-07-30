package com.projectForum.forum;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

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
	
}
