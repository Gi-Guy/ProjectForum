package com.projectForum.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.EditUserForm;
import com.projectForum.ControlPanel.Configuration.ForumInformation;
import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.PrivateMessages.Answer;
import com.projectForum.PrivateMessages.AnswerRepository;
import com.projectForum.PrivateMessages.Conversation;
import com.projectForum.PrivateMessages.ConversationRepository;
import com.projectForum.REST.DeleteUserForm;
import com.projectForum.forum.Forum;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;

/**
 * This Class will use as a service to all deletion option in the application 
 */
@Service
public class DeleteService {
	
	private AnswerRepository	answerRepo;
	private ConversationRepository	convRepo;
	
	private UserServices	userServices;
	private PostServices	postServices;
	private TopicServices	topicServices;
	private ForumServices	forumServices;
	private ForumInformationServices forumInformationServices;
	@Autowired
	public DeleteService(AnswerRepository answerRepo, ConversationRepository convRepo, UserServices userServices,
			PostServices postServices, TopicServices topicServices, ForumServices forumServices,
			ForumInformationServices forumInformationServices) {
		this.answerRepo = answerRepo;
		this.convRepo = convRepo;
		this.userServices = userServices;
		this.postServices = postServices;
		this.topicServices = topicServices;
		this.forumServices = forumServices;
		this.forumInformationServices = forumInformationServices;
	}
	
	/*
	 * ################################################################
	 * 						SCHEDULED
	 * ################################################################
	 * This section will define all scheduled methods that will delete
	 * old data.
	 * <minute> <hour> <day-of-month> <month> <day-of-week> <command>
	 * ################################################################
	 */
	
	public static final Logger LOG
    = LoggerFactory.getLogger(DeleteService.class);
	
	/**
	 * This Scheduled method will delete topics that didn't have any activity
	 * for more than ForumInformation.timeToDelete amount of time.
	 * This method will run daily at midnight.
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteOldTopics() {
		ForumInformation forumInformation = forumInformationServices.getForumInformation();
		int size = 0;
		// Notify action
		LOG.warn("[Scheduled method :: deleteOldTopics] "
				+ "Starting to delete topics that had Last activity "
				+ forumInformation.getTimeToDelete() + " days ago");
		
		List<Topic> topics = topicServices.findTopicBeforeDate(forumInformation.getTimeToDelete());
		// Checking if there are any topics to delete
		if (topics == null || topics.isEmpty()) {
			
			// Notify no action needed
			LOG.info("[Scheduled method :: deleteOldTopics] "
					+ "No topics needed to be deleted.");
			return;
		}
		
		// There are topics to delete (also delete posts if they exist)
		size = topics.size();
		this.deleteTopics(topics);
		// Notify job completed
		LOG.info("[Scheduled method :: deleteOldTopics] "
				+ "Job completed! \t"
				+size + " topics has been removed.");
	}
	
	/**
	 * This Scheduled method will delete private conversations that didn't have
	 * any activity for more than ForumInformation.timeToDelete amount of time.
	 * This method will run daily at midnight.
	 */
	@Scheduled(cron = "0 1 0 * * ?") // Delete daily at 00:00
	public void deleteOldConversations() {
		ForumInformation forumInformation = forumInformationServices.getForumInformation();
		int size = 0;
		// Notify action
		LOG.warn("[Scheduled method :: deleteOldConversations] "
				+ "Starting to delete private conversations that had Last activity "
				+ forumInformation.getTimeToDelete() + " days ago");
		
		List<Conversation> conversations = null;
		try {
			 conversations = convRepo.findByLastActivityBefore(
						LocalDateTime.now().minusDays(forumInformation.getTimeToDelete()));
		} catch (Exception e) {
			throw new EntityRequestException("Could not reload list of conversations by Date.");
		}
		// Checking if there are any conversations to delete
		if(conversations == null || conversations.isEmpty()){
			
			// Notify no action needed
			LOG.info("[Scheduled method :: deleteOldConversations] "
					+ "No conversations needed to be deleted.");
			return;
		}
		
		// There are conversations to delete (also delete posts if they exist)
		size = conversations.size();
		this.deleteConversations(conversations);
		// Notify job completed
		LOG.info("[Scheduled method :: deleteOldTopics] "
				+ "Job completed! \t"
				+size + " topics has been removed.");
		}
	
	/*
	 * ################################################################
	 * 						DELETE USERS
	 * ################################################################
	 * */
	/** 
	 * 			#####REST ONLY#####
	 * This method will delete an existing user.
	 * It will remove the user's data depending on the Keep activity flag.
	 *  
	 *  @see REST ONLY 
	 *  @return boolean true if deleted user.
	 */
	public boolean deleteUser(DeleteUserForm deleteUser) {
		User userByID = null;
		User userByUsername = null;
		if (deleteUser.getUserId() != 0)
			userByID = userServices.findUserByUserId(deleteUser.getUserId());
		if(!deleteUser.getUsername().isBlank())
			userByUsername = userServices.findUserByUsername(deleteUser.getUsername());
		
		// Checking if username and ID lead to same user
		if(userByID!=null && userByUsername!=null) {
			if(!userByID.equals(userByUsername))
				// ID and username are not the same user!
				return false;
		}
		
		// Checking if both are null
		if(userByID == null && userByUsername == null) {
			// User doesn't exist!
			return false;
		}
		// At this point one of the users isn't null
		if(userByUsername != null) {
			
			//	### USER'S PRIVATE MESSAGES MUST BE DELETED IN ANY CASE!
			// Deleting user's private messages
			List<Conversation> conversations = convRepo.findBySenderOrReceiver(userByUsername, userByUsername);
			this.deleteConversations(conversations);
			
			if(deleteUser.isKeepActivity())
				this.deleteUserKeepActivity(userByUsername);
			else
				this.deleteUserDontKeepActivity(userByUsername);	
		}
		else if(userByID != null) {
			
			//	### USER'S PRIVATE MESSAGES MUST BE DELETED IN ANY CASE!
			// Deleting user's private messages
			List<Conversation> conversations = convRepo.findBySenderOrReceiver(userByID, userByID);
			this.deleteConversations(conversations);
			
			
			if(deleteUser.isKeepActivity())
				this.deleteUserKeepActivity(userByID);
			else
				this.deleteUserDontKeepActivity(userByID);
		}
		
		return true;
	}
	

	/** 
	 * This method will delete an existing user.
	 * It will remove the user's data dependent on the Keep activity flag.
	 */
	public void deleteUser(EditUserForm editUser) {
		User user = userServices.findUserByUserId(editUser.getId());
		
		if(user == null)
			return;

		//	### USER'S PRIVATE MESSAGES MUST BE DELETED IN ANY CASE!
		// Deleting user's private messages
		List<Conversation> conversations = convRepo.findBySenderOrReceiver(user, user);
		this.deleteConversations(conversations);
		
		if(editUser.isKeepActivity())
			this.deleteUserKeepActivity(user);
		else
			this.deleteUserDontKeepActivity(user);
	}
	
	/** 
	 * This method will delete an existing user based on their username. However it won't delete 
	 * it posts and topics, but will attached user's posts and topics to an Dummy User.
	 */
	public void deleteUser(String username) {
		this.deleteUserKeepActivity(userServices.findUserByUsername(username));
	}

	/** 
	 * This method will delete an existing user based on their userID. However it won't delete 
	 * it posts and topics, but will attached user's posts and topics to an Dummy User.
	 */
	public void deleteUser(int userId) {
		this.deleteUserKeepActivity(userServices.findUserByUserId(userId));
	}

	/** 
	 * Private method that transfers the user's activity to a dummy user.
	 */
	private void deleteUserKeepActivity(User user) {
		
		User dummyUser = userServices.findUserByUsername("Unknown");
		
		// Making sure that dummy user won't be deleted or Admin user
		if(dummyUser.equals(user) || user.getRoles().iterator().next().getName().equals("ADMIN"))
			return;
		
		// Removing Role (many To many, need to be removed)
		user.removeRole();
		
		// Change the topic and post to dummy owner.
		List<Post>	userPosts	=	postServices.findPostsByUser(user);
		List<Topic>	userTopics	=	topicServices.findTopicsByUser(user);
		
		// posts:
		if(!userPosts.isEmpty())
			for (int i=0; i<userPosts.size(); i++) {
				userPosts.get(i).setUser(dummyUser);
			}
		// topics:
		if(!userTopics.isEmpty())
			for (int i=0; i<userTopics.size(); i++) {
				userTopics.get(i).setUser(dummyUser);
			}
		userServices.delete(user);
		
	}
	
	/** 
	 * Private method that deletes all the user's activity
	 */
	private void deleteUserDontKeepActivity(User user) {
		List<Post>	userPosts	=	postServices.findPostsByUser(user);
		List<Topic>	userTopics	=	topicServices.findTopicsByUser(user);
		
		User dummyUser = userServices.findUserByUsername("Unknown");
		
		// Making sure that dummy user won't be deleted or Admin user
		if(dummyUser.equals(user) || user.getRoles().iterator().next().getName().equals("ADMIN"))
			return;
		
		// Removing Role (many To many, need to be removed)
		user.removeRole();
		
		// Removing all posts and topics
		//posts:
		if(!userPosts.isEmpty()) {
			this.deletePosts(userPosts);	
		}
		//topics:
		if(!userTopics.isEmpty()) {
			this.deleteTopics(userTopics);	
		}
		// Removing user
		userServices.delete(user);
	}

	/*
	 * ################################################################
	 * 						DELETE POSTS
	 * ################################################################
	 */
	
	/** 
	 * This method will delete a post by postId
	 * @param int postId
	 */
	public void deletePost (int postId) {
		Post post = postServices.findPostById(postId);
		postServices.delete(post);
	}
	
	/** 
	 * This method will delete a given post.
	 * @param Post - post to delete
	 */
	public void deletePost(Post post) {
		postServices.delete(post);
	}
	
	/** 
	 * This method will delete a given list of posts.
	 * @param List<Post>
	 */
	public void deletePosts(List<Post> posts) {

		if(posts.isEmpty())
			return;
		for(int i=0; i<posts.size(); i++)
			this.deletePost(posts.get(i));
	}
	
	/*
	 * ################################################################
	 * 						DELETE TOPICS
	 * ################################################################
	 */
	
	/** 
	 * This method will delete a topic and it posts by topicId.
	 * @param int topicId
	 */
	public void deleteTopic(int topidId) {
		Topic topic = topicServices.findTopicById(topidId);
		
		this.deleteTopic(topic);
	}
	
	/** 
	 * This method will delete a topic and it posts by topic object.
	 * Deleting a topic requires deleting all posts attached to it. 
	 */
	public void deleteTopic(Topic topic) {

		// If the topic has posts then delete them too
		List<Post> posts = postServices.findPostsByTopic(topic);
		if(!posts.isEmpty())
			this.deletePosts(posts);
		
		topicServices.delete(topic);
	}
	
	/** This method will delete a list of topics */
	public void deleteTopics(List<Topic> topics) {

		if(topics.isEmpty())
			return;
		
		for (int i=0; i<topics.size(); i++)
			this.deleteTopic(topics.get(i));
	}
	
	/*
	 * ################################################################
	 * 						DELETE FORUMS
	 * ################################################################
	 */
	/** 
	 * This method will delete a forum given its forumId
	 */
	public void deleteForum(int forumId) {
		Forum forum = forumServices.findFourmById(forumId);
		this.deleteForum(forum);
	}
	
	/** 
	 * This method will delete a forum represented by the Forum object.
	 * Deleting a forum requires deleting all topics attached to it. 
	 */
	public void deleteForum(Forum forum) {
		if(forum == null)
			return;
		
		this.updateAllLowerPriority(forum);
		
		// Delete all topics in the forum
		List<Topic> topics = topicServices.findTopicsByForum(forum);
		this.deleteTopics(topics);
		
		forumServices.delete(forum);
	}
	
	/** This method will delete a list of forums */
	public void deleteForums(List<Forum> forums) {

		if(forums.isEmpty())
			return;
		for(int i=0; i<forums.size(); i++)
			this.deleteForum(forums.get(i));
	}
	
	/** 
	 * This method will delete all the forums + Topics + Posts in database.
	 * @warning THIS CAN'T BE UNDONE
	 * 
	 */
	public void deleteAllForums() {
		
		List<Forum> forums = forumServices.findAll();
		this.deleteForums(forums);
	}
	
	/**
	 * When deleting a forum there is need to update all lower priority 
	 */
	private void updateAllLowerPriority(Forum forum) {
		final int forumsSize = forumServices.findForumsByPriorityAsc().size();
		
		// if true then no need to update any other forum's priority
		if (forum.getPriority() == forumsSize)
			return;
		
		// forum isn't in last priority, need to update all lower priority forums
		for (int i = forum.getPriority() + 1; i <= forumsSize; i++) {
			Forum updateForum = forumServices.findForumByPriority(i);
			updateForum.setPriority(i - 1);
			forumServices.save(updateForum);
		}
	}
	
	/*
	 * ################################################################
	 * 						DELETE ANSWERS
	 * ################################################################
	 */
	
	/**
	 * This method will delete an answer by answerId
	 * @param int
	 */
	public void deleteAnswer(int answerId) {
		Answer answer = null;
		
		try {
			answer = answerRepo.findById(answerId);
		} catch (Exception e) {
			throw new EntityRequestException("Could not reload Answer id: " + answerId);
		}
		
		if (answer!=null)
			this.deleteAnswer(answer);
	}
	
	/**
	 * This method will delete a given answer in a conversation
	 * @param Answer
	 */
	public void deleteAnswer(Answer answer) {
		if (answer!=null)
			try {
				answerRepo.delete(answer);
			} catch (Exception e) {
				throw new EntityRequestException("Could not delete Answer id: " + answer.getId());
			}
	}
	
	/**
	 * This method will delete a list of answers 
	 * @param List<Answer>
	 */
	public void deleteAnswer(List<Answer> answers) {
		
		if(answers.isEmpty())
			return;
		
		for (int i=0; i<answers.size(); i++)
			this.deleteAnswer(answers.get(i));
	}
	
	/*
	 * ################################################################
	 * 						DELETE CONVERSATION
	 * ################################################################
	 */
	
	/**
	 * This method will delete a conversation by id
	 * @param int 
	 */
	public void deleteConversation(int conId) {
		Conversation conversation = null;
		
		try {
			conversation = convRepo.findById(conId);
		} catch (Exception e1) {
			throw new EntityRequestException("Could not reload Conversation id: " + conId);
		}
		
		if(conversation!=null)
			try {
				this.deleteConversation(conversation);
			} catch (Exception e) {
				throw new EntityRequestException("Could not delete Conversation id: " + conId);
			}
	}
	
	/**
	 * This method will delete a given conversation
	 * @param Conversation
	 */
	public void deleteConversation(Conversation conversation) {
		if (conversation != null) {
			List<Answer> answers = null;
			
			try {
				answers = answerRepo.findByConversation(conversation);
			} catch (Exception e1) {
				throw new EntityRequestException("Could not find answers for Conversation id:  " + conversation.getId());
			}
			
			this.deleteAnswer(answers);
			try {
				convRepo.delete(conversation);
			} catch (Exception e) {
				throw new EntityRequestException("Could not delete Conversation id: " + conversation.getId());
			}
		}
	}
	
	public void deleteConversations(List<Conversation> conversations) {
		if(conversations.isEmpty())
			return;
		for(int i=0; i<conversations.size(); i++) {
			this.deleteConversation(conversations.get(i));
		}
	}
}