package com.projectForum.topic;



import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projectForum.Exceptions.AccessDeniedRequestException;
import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.Services.DeleteService;
import com.projectForum.Services.EditServices;
import com.projectForum.Services.ForumServices;
import com.projectForum.Services.PostServices;
import com.projectForum.Services.TopicServices;
import com.projectForum.Services.UserServices;
import com.projectForum.post.Post;


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
	
	private UserServices	userServices;
	private PostServices	postServices;
	private ForumServices	forumServices;
	private DeleteService	deleteService;
	private EditServices	editService;
	private TopicServices	topicServices;
	
	private AccessDeniedRequestException accessDeniedRequestException = new AccessDeniedRequestException();
	private final String localUrl = "/topic/";

	@Autowired
	public TopicController(UserServices userServices, TopicServices topicServices, PostServices postServices,
			ForumServices forumServices, DeleteService deleteService, EditServices editService) {
		this.userServices = userServices;
		this.topicServices = topicServices;
		this.postServices = postServices;
		this.forumServices = forumServices;
		this.deleteService = deleteService;
		this.editService = editService;
	}

	/**This method will display a full topic page including:
	 * @return Model:	Topic object with all information
	 * @return Model:	List<Post> - list of all posts that in Topic object
	 * @return Model:	Post - new Post object for replay option
	 * */
	@GetMapping("{topicId}")
	public String getTopicById(@PathVariable int topicId, Model model) {
		
		Topic topic = topicServices.findTopicById(topicId);
		List<Post> posts = postServices.findPostsByTopic(topic);
		
		if(topic == null)
			throw new EntityRequestException("Could not display topic ID :: " + topicId);
		if(posts == null)
			throw new EntityRequestException("Could not find posts for topic ID :: " + topicId);
		
		// Each view have to update the views counter by 1
		topic.setViews(topic.getViews() + 1);
		topicServices.save(topic);
		
		model.addAttribute("topic", topic);
		// Each topic can have 0 or more posts in it
		model.addAttribute("posts", posts);
		// In each topic there is an option to create a new post
		model.addAttribute("newPost", new Post());
		
		return "topic";
	}
	
	/**This method will add a new post to an exists topic*/
	@PostMapping("{topicId}")
	public String addNewPost(@Valid @ModelAttribute Post post, BindingResult bindingResult, @PathVariable int topicId,
								Authentication authentication, Model model) {
		
		// If hasErrors == true, then return to topic page, because something went wrong
		if(bindingResult.hasErrors()) {
			System.err.println("ERROR :: Topic Controller - addNewPost (POST)");
			// If there is an error we should go back to topic page and try again.
			model.addAttribute("topic", topicServices.findTopicById(topicId));
			model.addAttribute("posts", postServices.findPostsByTopicId(topicId));
			model.addAttribute("newPost", new Post());
			// Keeping user in same page to fix issues
			return "topic";
		}
		
		// No errors, creating a new post in topic
		post.setUser(userServices.findUserByUsername(authentication.getName()));
		post.setTopic(topicServices.findTopicById(topicId));
		postServices.save(post);

		model.asMap().clear(); // Cleaning the model as it does some weird things if not.
		return "redirect:" + topicId + '#' + post.getId(); // User will be redirected to the post they wrote.
	}
	
	/** This method will return a model and navigate the user to newTopic page */
	@GetMapping("newTopic/{forumId}")
	public String createNewTopicInForum(@PathVariable int forumId, Model model) {
		// Using a newTopicform to keep our forumId.
		NewTopicPageForm newTopic = new NewTopicPageForm();
		// Saving forumId value.
		newTopic.setForumId(forumId);
		
		model.addAttribute("newTopic", newTopic);
		return "new_Topic_page";
	}
	
	/** This method will create a new topic and navigate the user to the new topic page. */
	@PostMapping("newTopic")
	public String proccesNewTopic(@Valid @ModelAttribute("newTopic") NewTopicPageForm newTopic, BindingResult bindingResult, Authentication authentication, Model model) {
		
		// If hasErrors == true, then return to topic page, because something went wrong
		if(bindingResult.hasErrors()) {
			System.err.println("ERROR :: Topic Controller - proccesNewTopic (POST)");
			// If there is an error we should go back to topic creation page and try again.
			// TODO I'm not really sure what should we put into model.
			return "new_Topic_page";
		}
		// Checking if title or content are blanked.
		if(newTopic.getContent().isBlank() || newTopic.getTitle().isBlank())
			return "new_Topic_page";
		
		// Creating new Topic
		Topic topic = new Topic();
		topic.setTitle(newTopic.getTitle());
		topic.setContent(newTopic.getContent());
		topic.setForum(forumServices.findFourmById(newTopic.getForumId()));
		topic.setUser(userServices.findUserByUsername(authentication.getName()));
		topic.setClosed(false);
		topic.setViews(0);
		
		
		topicServices.save(topic);
		
		return "redirect:/topic/" + topic.getId();
	}
	
	/**This method will enter user to edit mode for exsits topic*/
	@GetMapping("edit/{topicId}")
	public String editTopic(@PathVariable int topicId, Model model, Authentication authentication) {
		Topic topic = topicServices.findTopicById(topicId);
		//	Checking if user allowed to edit Topic
		if(!authentication.getName().equals(topic.getUser().getUsername()) 
				|| !userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			//	User isn't allowed to edit Topic
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "edit/" + topicId);
			
		// Using newTopicForm to keep the topicId while editing.
		NewTopicPageForm editTopic = new NewTopicPageForm();
		editTopic.setTopicId(topicId);
		model.addAttribute("editTopic", editTopic);
		return "edit_Topic_page";
	}
	
	@PostMapping("editTopic")
	public String editTopic(@Valid @ModelAttribute("editTopic") NewTopicPageForm editTopic, BindingResult bindingResult, Authentication authentication, Model model) {
		
		Topic topic = topicServices.findTopicById(editTopic.getTopicId());
		
		// Approving admin or allowed user
		if(authentication.getName().equals(topic.getUser().getUsername()) 
			|| userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			editService.updateTopic(topic, editTopic);
		else	
			//user isn't allowed to edit this topic
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "editTopic" + editTopic.getTopicId());

		return "redirect:/topic/" + topic.getId();
	}
	
	
	/** This method will delete an exists method.
	 * 	A Topic can't be deleted as long List<Post> is not empty */
	
	@GetMapping("delete/{topicId}")
	public String deleteTopic(@PathVariable int topicId, Authentication authentication,
									RedirectAttributes model) {
		// find topic to remove and all it posts
		Topic topic = topicServices.findTopicById(topicId);
		
		//	Checking if topic is exists
		if(topic == null || authentication == null)
			throw new EntityRequestException("Something went wrong, could not reload topic for topic :: '" + topicId+"'");
		//	Making sure that user's allowed to delete topic
		else if(!authentication.getName().equals(topic.getUser().getUsername())
				|| !userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "delete/" + topicId);
		
		deleteService.deleteTopic(topic);
		model.addFlashAttribute("message", "Topic has been removed.");
		return "redirect:/forum/" + topic.getForum().getId();
	}
	
}