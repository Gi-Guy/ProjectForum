package com.projectForum.topic;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.projectForum.forum.Forum;
import com.projectForum.user.User;

/** Topic is a collection of posts,
 * However it has a main content just like a regular post, but it won't be count as a Post but as a topic content.
 * 
 * Each Topic has a user who created it.
 * Each Post has to be attached to a topic, A post can't be a ghost Post.
 * Each Topic has to be attached to a topics page (Forum), A topic can't be a ghost Topic */
@Entity
@Table(name = "topic")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Topic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(updatable = true, nullable = false, length = 50)
	private String title;
	
	@Column(updatable = true, nullable = false, columnDefinition = "TEXT")
	private String content;
	
	/* User Author Information */
	@ManyToOne
	@JoinColumn(name="user_id",referencedColumnName = "id")
	@JsonManagedReference
	private User user;
	
	@ManyToOne
	@JoinColumn(name="forum_id",referencedColumnName = "id")
	@JsonManagedReference
	private Forum forum;
	
	@Column
	private int views;
	
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdDate;
	
	@Column(updatable = true, nullable = false)
	private LocalDateTime lastActivity;
	
	@Column
	private boolean closed;
	
	@PrePersist
	 protected void onCreate() {
		 this.createdDate = LocalDateTime.now();
		 this.lastActivity = LocalDateTime.now();
	 }
	
	/* @PreUpdate
	protected void onUpdate() {
		this.lastActivity = LocalDateTime.now();
	}*/
	 
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public int getViews() {
		return views;
	}
	
	public void setViews(int views) {
		this.views = views;
	}
	
	public LocalDateTime getLastActivity() {
		return lastActivity;
	}
	
	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public Forum getForum() {
		return forum;
	}

	public void setForum(Forum forum) {
		this.forum = forum;
	}
    //Auto generated 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (closed ? 1231 : 1237);
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((forum == null) ? 0 : forum.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastActivity == null) ? 0 : lastActivity.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + views;
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
		Topic other = (Topic) obj;
		if (closed != other.closed)
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (forum == null) {
			if (other.forum != null)
				return false;
		} else if (!forum.equals(other.forum))
			return false;
		if (id != other.id)
			return false;
		if (lastActivity == null) {
			if (other.lastActivity != null)
				return false;
		} else if (!lastActivity.equals(other.lastActivity))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (views != other.views)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Topic [id=" + id + ", title=" + title + ", content=" + content 
				+ ", views=" + views + ", createdDate=" + createdDate + ", lastActivity=" + lastActivity + ", closed="
				+ closed + "]";
	}
	
	
}