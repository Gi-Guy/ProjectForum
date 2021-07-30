package com.projectForum.post;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import com.projectForum.topic.TopicRepository;
import com.projectForum.user.UserRepository;

/**
 * This controller will handle the next actions:
 * Find all Posts by id 
 * Delete a post by id
 * */

@Controller
//@RequestMapping(value = "/post")
public class PostController {
	
	
	private UserRepository 	userRepo;
	private PostRepository postRepo;
	private TopicRepository topicRepo;
	

	@Autowired
	public PostController(UserRepository userRepo, PostRepository postRepo, TopicRepository topicRepo) {
		super();
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.topicRepo = topicRepo;
	}



	/**This method will add a new post to an exists topic*/
	@PostMapping("/topic/{topicId}")
	public String addNewPost(@Valid @ModelAttribute Post post, BindingResult bindingResult, @PathVariable int topicId,
								Authentication authentication, Model model) {
		// If hasErrors == true, then return to topic page.
		if(bindingResult.hasErrors()) {
			System.out.println("I'm here (New post)  and I don't know what to do with my life.");
			// TODO: Solve this issue <------------------------------------------------------FIX THIS!
		}
		
		// No errors, creating a new post in topic
		// TODO check if new post register dates.
		post.setUser(userRepo.findByUsername(authentication.getName()));
		post.setTopic(topicRepo.findTopicById(topicId));
		postRepo.save(post);

		model.asMap().clear();//Cleaning model because it don't some weird things if not.
		return "redirect:/topic/" + topicId; //User will return to the same topic page
	}
}