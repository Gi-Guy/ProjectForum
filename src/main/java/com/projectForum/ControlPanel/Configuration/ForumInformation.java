package com.projectForum.ControlPanel.Configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 	This Entity represents the forum web application itself and some configurations*/

@Entity
@Table(name = "ForumInformation")
public class ForumInformation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, unique = true, length = 50)
	private String name;
	
	@Column(nullable = false, length = 200)
	private String description;
	
	@Column(nullable = false)
	private int timeToDelete;
	
	

	public ForumInformation() {
		
	}
	public ForumInformation(String name, String description, int timeToDelete) {
		this.name = name;
		this.description = description;
		this.timeToDelete = timeToDelete;
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

	public int getTimeToDelete() {
		return timeToDelete;
	}

	public void setTimeToDelete(int timeToDelete) {
		this.timeToDelete = timeToDelete;
	}
	
	public int getId() {
		return id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + timeToDelete;
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
		ForumInformation other = (ForumInformation) obj;
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
		if (timeToDelete != other.timeToDelete)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ForumInformation [id=" + id + ", name=" + name + ", description=" + description + ", timeToDelete="
				+ timeToDelete + "]";
	}
	
	
}
