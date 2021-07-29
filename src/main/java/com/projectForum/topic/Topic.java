package com.projectForum.topic;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.projectForum.post.Post;
import com.projectForum.user.User;

/** Topic is a collection of posts,
 * However it has a main content just like a regular post, but it won't be count as a Post but as a topic content.
 * 
 * Each Topic has a user who created it.
 * Each Post has to be attached to a topic, A post can't be a ghost Post.
 * Each Topic has to be attached to a topics page (Forum), A topic can't be a ghost Topic */
@Entity
@Table(name = "topic")
public class Topic {
	
	// TODO: add a forum link or a category.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(updatable = true, nullable = false, length = 50)
	private String title;
	
	@Column(updatable = true, nullable = false, columnDefinition = "TEXT")
	private String content;
	
	/* User Author Information */
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column
	private int views;
	
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdDate;
	
	@Column(updatable = true, nullable = false)
	private LocalDateTime lastActivity;
	
	@Column
	private boolean closed;
	
	// EACH TOPIC CAN HAVE {POSTS}* (0 or more)
	// @OneToMany(mappedBy = "topic")
	// private List<Post> posts;
	
	/* public List<Post> getPosts() {
		return posts;
	}
	
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}*/
	
	@PrePersist
	 protected void onCreate() {
		 this.createdDate = LocalDateTime.now();
		 this.lastActivity = LocalDateTime.now();
	 }
	
	 @PreUpdate
	protected void onUpdate() {
		this.lastActivity = LocalDateTime.now();
	}
	 
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
	
	/*public List<Post> getPosts() {
		return posts;
	}
	
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}*/
	
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
}