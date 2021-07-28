package com.projectForum.topic;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.projectForum.post.Post;
import com.projectForum.post.PostRepository;

import com.projectForum.user.UserRepository;


/**
 * This controller will handle the next actions:
 * Find Topic by id
 * find Topic by Username
 * Add new post to an exists Topic
 * Create new Topic
 * Delete topic - If this method isn't here, Check controlPanel Controller.*/

@Controller
@RequestMapping("/topic/")
public class TopicController {
	
	private UserRepository 	userReop;
	private TopicRepository topicRepo;
	private PostRepository 	postRepo;
	
	
	@Autowired
	public TopicController(UserRepository userReop, TopicRepository topicRepo, PostRepository postRepo) {
		this.userReop = userReop;
		this.topicRepo = topicRepo;
		this.postRepo = postRepo;
	}
	@GetMapping("{topicId}")
	public String getTopicById(@PathVariable int topicId, Model model) {
		
		Topic topic = topicRepo.findTopicById(topicId);
		//Each call we update the views counter by 1
		topic.setViews(topic.getViews() + 1);
		topicRepo.save(topic);
		
		model.addAttribute("topic", topic);
		//Each topic can have 0 or more posts in it
		model.addAttribute("posts", postRepo.findPostsByTopicId(topicId));
		//in each topic there is an option to create a new post
		model.addAttribute("newPost", new Post());
		
		return "topic";
	}
	// TODO finish this method.
	@GetMapping("{username}")
	public String getTopicByUsername() {
		
		return "";
	}
	/**This method will add a new post to an exists topic*/
	@PostMapping("{topicId}")
	public String addNewPost(@Valid @ModelAttribute Post post, BindingResult bindingResult, @PathVariable int topicId,
								Authentication authentication, Model model) {
		//if hasErrors == true, then return to topic page.
		if(bindingResult.hasErrors()) {
			System.out.println("I'm here (New post)  and I don't know what to do with my life.");
			// TODO: Solve this issue <------------------------------------------------------FIX THIS!
		}
		
		//No errors, creating a new post in topic
		// TODO check if new post register dates.
		post.setUser(userReop.findByUsername(authentication.getName()));
		post.setTopic(topicRepo.findTopicById(topicId));
		postRepo.save(post);

		model.asMap().clear();//Cleaning model because it don't some weird things if not.
		return "redirect:/topic/" + topicId; //User will return to the same topic page
	}
	
	/**This method will return a model and navigate the user to newTopic page*/
	@GetMapping("newTopic")
	public String createNewTopic(@Valid @ModelAttribute Topic topic, Model model) {
		model.addAttribute("newTopic", new Topic());
		// TODO: I think I need to add a forum --> EACH TOPIC HAS TO BE ATTACHED TO A FORUM.
		
		return "new_Topic_page";
	}
	
	/**This method will create a new topic and navigate the user to the new topic page.*/
	@PostMapping("newTopic")
	public String proccesNewTopic(@Valid @ModelAttribute Topic topic, BindingResult bindingResult, Authentication authentication, Model model) {
		
		//if hasErrors == true, then return to topic page. something went wrong.
		if(bindingResult.hasErrors()) {
			System.out.println("I'm here (New Topic)  and I don't know what to do with my life.");
			// TODO: Solve this issue <------------------------------------------------------FIX THIS!
		}
		
		//No issues, Creating new Topic
		// TODO ERROR! ATTACHE IT TO A FORUM!
		topic.setUser(userReop.findByUsername(authentication.getName()));
		topic.setClosed(false);
		topic.setViews(0);
		topicRepo.save(topic);
		
		return "redirect:/topic/" + topic.getId();
	}
	
	// TODO Move this method to control panel?
	// TODO finish this method.
	/**This method will delete an exists method*/
	@GetMapping("delete/{topicId}")
	public String deleteTopic(@PathVariable int topicId, Authentication authentication) {
		return "";
	}
	
}
