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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projectForum.Services.DeleteService;
import com.projectForum.Services.EditServices;
import com.projectForum.forum.ForumRepository;
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
	
	private UserRepository 	userRepo;
	private TopicRepository topicRepo;
	private PostRepository 	postRepo;
	private ForumRepository forumRepo;
	private DeleteService	deleteService;
	private EditServices	editService;
	

	@Autowired
	public TopicController(UserRepository userRepo, TopicRepository topicRepo, PostRepository postRepo,
			ForumRepository forumRepo, DeleteService deleteService, EditServices editService) {
		this.userRepo = userRepo;
		this.topicRepo = topicRepo;
		this.postRepo = postRepo;
		this.forumRepo = forumRepo;
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
		
		Topic topic = topicRepo.findTopicById(topicId);
		// Each view have to update the views counter by 1
		topic.setViews(topic.getViews() + 1);
		topicRepo.save(topic);
		
		model.addAttribute("topic", topic);
		// Each topic can have 0 or more posts in it
		model.addAttribute("posts", postRepo.findPostsByTopicId(topicId));
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
			model.addAttribute("topic", topicRepo.findTopicById(topicId));
			model.addAttribute("posts", postRepo.findPostsByTopicId(topicId));
			model.addAttribute("newPost", new Post());
			// Keeping user in same page to fix issues
			return "topic";
		}
		
		// No errors, creating a new post in topic
		post.setUser(userRepo.findByUsername(authentication.getName()));
		post.setTopic(topicRepo.findTopicById(topicId));
		postRepo.save(post);

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
		topic.setForum(forumRepo.findById(newTopic.getForumId()));
		topic.setUser(userRepo.findByUsername(authentication.getName()));
		topic.setClosed(false);
		topic.setViews(0);
		
		
		topicRepo.save(topic);
		
		return "redirect:/topic/" + topic.getId();
	}
	
	/**This method will enter user to edit mode for exsits topic*/
	@GetMapping("edit/{topicId}")
	public String editTopic(@PathVariable int topicId, Model model) {
		// Using newTopicForm to keep the topicId while editing.
		NewTopicPageForm editTopic = new NewTopicPageForm();
		editTopic.setTopicId(topicId);
		model.addAttribute("editTopic", editTopic);
		// TODO Find out how to open a edit mode section and not using a new page.
		return "edit_Topic_page";
	}
	
	@PostMapping("editTopic")
	public String editTopic(@Valid @ModelAttribute("editTopic") NewTopicPageForm editTopic, BindingResult bindingResult, Authentication authentication, Model model) {
		
		Topic topic = topicRepo.getById(editTopic.getTopicId());
		
		// Approving admin or allowed user
		if(authentication.getName().equals(topic.getUser().getUsername()) 
				|| userRepo.findByUsername(authentication.getName()).getRoles().iterator().next().getName().equals("ADMIN"))
			editService.updateTopic(topic, editTopic);

		return "redirect:/topic/" + topic.getId();
	}
	
	
	/** This method will delete an exists method.
	 * 	A Topic can't be deleted as long List<Post> is not empty */
	
	@GetMapping("delete/{topicId}")
	public String deleteTopic(@PathVariable int topicId, Authentication authentication,
									RedirectAttributes model) {
		// find topic to remove and all it posts
		Topic topic = topicRepo.findTopicById(topicId);
		
		// making sure that topic is exists and user allowed to remove it or Admin		
		if (topic == null || authentication == null || !authentication.getName().equals(topic.getUser().getUsername())
				|| !userRepo.findByUsername(authentication.getName()).getRoles().iterator().next().getName().equals("ADMIN")) {
			//topic can't be removed
			return "redirect:/";
		}
		
		deleteService.deleteTopic(topic);
		model.addFlashAttribute("message", "Topic has been removed.");
		return "redirect:/forum/" + topic.getForum().getId();
	}
	
}