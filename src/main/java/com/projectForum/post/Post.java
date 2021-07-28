package com.projectForum.post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.projectForum.topic.Topic;
import com.projectForum.user.User;

@Entity
@Table(name = "post")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdDate;
	
	@Column(updatable = true, nullable = false)
	private LocalDateTime lastActivity;
	
	/*User Author Information*/
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column (nullable = false,columnDefinition = "TEXT")
	private String content;
	
	//EACH POST HAS TO BE ATTCHED TO A TOPIC
	//EACH TOPIC CAN HAVE MANY POSTS
	@ManyToOne
	@JoinColumn(name = "id_topic")
	 private Topic topic;
	
	
	@PreUpdate
	protected void onUpdate() {
		this.lastActivity = LocalDateTime.now();
	}
	
	
	public Topic getTopic() {
		return topic;
	}


	public void setTopic(Topic topic) {
		this.topic = topic;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String conent) {
		this.content = conent;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createDate) {
		this.createdDate = createDate;
		this.lastActivity = createDate;
	}

	public LocalDateTime getLastActivity() {
		return lastActivity;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
    public String displayParsedCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd.MM.yyyy");
        return this.createdDate.format(formatter);
    }
    
    //TODO upadte this methode after finishing Topics and Forums
    /**
     * Doing a deep comparison of both objectives.*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if(obj==null)
			return true;
		
		if(getClass()!=obj.getClass())
			return false;
		 
		//Doing a deep comparison now
		Post newPost = (Post) obj;
		
		//content should never be a null
		if(this.content == null) {
			if(newPost.content!=null)
				return false;
		}
		else if(!this.content.equals(newPost.content))
			return false;
		//This is also should not be null
		if(this.createdDate == null) {
			if(newPost.createdDate!=null)
				return false;
		}
		else if(!this.createdDate.equals(newPost.createdDate))
			return false;
		if(this.id!=newPost.id)
			return false;
		//This is also should not be null
		if(this.lastActivity==null) {
			if(newPost.lastActivity!=null)
				return false;
		}
		else if(!this.lastActivity.equals(newPost.lastActivity))
			return false;

		//TODO add topics check
		
		if(this.user==null) {
			if(newPost.user!=null)
				return false;
		}
		else if(!user.equals(newPost.user))
			return false;
		
		return true;
	}
    
    
    //TODO add quick display for contect
    
}
