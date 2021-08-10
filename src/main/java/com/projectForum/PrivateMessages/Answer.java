package com.projectForum.PrivateMessages;

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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectForum.user.User;

@Entity
@Table(name = "answer")
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@JsonManagedReference
	private User user;
	
	@Column (nullable = false, columnDefinition = "TEXT")
	private String answer;
	
	@ManyToOne
	@JoinColumn(name = "id_conversation")
	@JsonManagedReference
	 private Conversation conversation;
	
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdDate;
	
	@Column(updatable = true, nullable = false)
	private LocalDateTime lastActivity;
	
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((conversation == null) ? 0 : conversation.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastActivity == null) ? 0 : lastActivity.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Answer other = (Answer) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (conversation == null) {
			if (other.conversation != null)
				return false;
		} else if (!conversation.equals(other.conversation))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (id != other.id)
			return false;
		if (lastActivity == null) {
			if (other.lastActivity != null)
				return false;
		} else if (!lastActivity.equals(other.lastActivity))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Answer [id=" + id + ", user=" + user.getUsername() + ", answer=" + answer + ", conversation=" + conversation
				+ ", createdDate=" + createdDate + ", lastActivity=" + lastActivity + "]";
	}
	
	
}