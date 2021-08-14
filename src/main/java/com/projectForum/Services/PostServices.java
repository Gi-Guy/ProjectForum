package com.projectForum.Services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.post.Post;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;

/**
 * 	This class will gives services for most post's actions
 * */

@Service
public class PostServices {

	@Autowired	
	private PostRepository	postRepo;
	@Autowired
	TopicServices	topicServices;
	
	public Post findPostById(int postId) throws EntityRequestException{
		Post post = null;
		
		try {
			post = postRepo.findById(postId);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find a post by id");
		}
		
		//if (post == null)
			//throw new EntityRequestException("Could not find a post by id");
		
		return post;
	}
	
	public List<Post> findPostsByTopicId(int topicId){
		return this.findPostsByTopic(topicServices.findTopicById(topicId));
	}
	
	public List<Post> findPostsByTopic(Topic topic) throws EntityRequestException{
		List<Post> posts = null;
		
		try {
			posts = postRepo.findPostsByTopic(topic);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find posts by topic");
		}
		
	//	if (posts == null)
		//	throw new EntityRequestException("Could not find posts by topic");
		
		return posts;
	}
	
	public List<Post> findPostsByUser(User user) throws EntityRequestException{
		List<Post> posts = null;
		
		try {
			posts = postRepo.findPostsByUser(user);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find posts by user");
		}
		//if(posts == null)
		//	throw new EntityRequestException("Could not find posts by user");
		
		return posts;
	}
	public List<Post> findAll() throws EntityRequestException{
		List<Post> posts = null;
		
		try {
			posts = postRepo.findAll();
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all posts");
		}
		
		return posts;
	}
	public void save(Post post) throws EntityRequestException{
		try {
			postRepo.save(post);
		} catch (Exception e) {
			throw new EntityRequestException("Could not save new post");
		}
	}
	public void delete(Post post) throws EntityRequestException{
		try {
			postRepo.delete(post);
		} catch (Exception e) {
			throw new EntityRequestException("Could not delete post");
		}
	}
}
