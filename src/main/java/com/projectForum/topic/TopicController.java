package com.projectForum.topic;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@Autowired
	public TopicController(UserRepository userReop, TopicRepository topicRepo, PostRepository postRepo,
			ForumRepository forumRepo) {
		this.userRepo = userReop;
		this.topicRepo = topicRepo;
		this.postRepo = postRepo;
		this.forumRepo = forumRepo;
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
	
	/** This method will return a model and navigate the user to newTopic page */
	@GetMapping("newTopic")
	public String createNewTopic(@Valid @ModelAttribute Topic topic, Model model) {
		//model.addAttribute("newTopic", new Topic());
		model.addAttribute("newTopic", new NewTopicPageForm());
		// TODO fix this
		model.addAttribute("forums",forumRepo.findAll());
		
		return "new_Topic_page";
	}

	@GetMapping("newTopic/{forumId}")
	public String createNewTopicInForum(@Valid @ModelAttribute Topic topic, @PathVariable int forumId, Model model) {
		NewTopicPageForm newTopic = new NewTopicPageForm();
		newTopic.setForumId(forumId);
		
		model.addAttribute("newTopic", newTopic);
		
		return "new_Topic_page";
	}
	
	/** This method will create a new topic and navigate the user to the new topic page. */
	@PostMapping("newTopic")
	public String proccesNewTopic(@Valid @ModelAttribute("newTopic") NewTopicPageForm newTopic, BindingResult bindingResult, Authentication authentication, Model model) {
		
		// If hasErrors == true, then return to topic page. something went wrong.
		if(bindingResult.hasErrors()) {
			System.out.println("I'm here (New Topic) and I don't know what to do with my life.");
			// TODO: Solve this issue <------------------------------------------------------FIX THIS!
		}
		
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
	public String editTopic(@Valid @ModelAttribute("newTopic") Topic topic,@PathVariable int topicId, Model model) {
		NewTopicPageForm newTopic = new NewTopicPageForm();
		newTopic.setTopicId(topicId);
		model.addAttribute("newTopic", newTopic);
		return "edit_Topic_page";
	}
	
	@PostMapping("editTopic")
	public String editTopic(@Valid @ModelAttribute("newTopic") NewTopicPageForm newTopic, BindingResult bindingResult, Authentication authentication, Model model) {
		
		Topic topic = topicRepo.getById(newTopic.getTopicId());
		
		if(authentication.getName().equals(topic.getUser().getUsername())){
			if(!newTopic.getTitle().isEmpty())
				topic.setTitle(newTopic.getTitle());
			
			if(!newTopic.getContent().isEmpty())
				topic.setContent(newTopic.getContent());
			
			topicRepo.save(topic);
		}
		return "redirect:/topic/" + topic.getId();
	}
	
	
	/** This method will delete an exists method.
	 * 	A Topic can't be deleted as long List<Post> is not empty */
	@GetMapping("delete/{topicId}")
	public String deleteTopic(@PathVariable int topicId, Authentication authentication,
									RedirectAttributes model) {
		// find topic to remove and all it posts
		Topic topic = topicRepo.findTopicById(topicId);
		List<Post> posts = postRepo.findPostsByTopic(topic);
		
		// making sure that topic is exists and user allowed to remove it		
		if (topic == null || authentication == null || !authentication.getName().equals(topic.getUser().getUsername())) {
			//topic can't be removed
			return "redirect:/";
		}
		/* at this point topic can be removed and posts as well.*/
		// Removing posts
		if(posts != null) {
			while( !posts.isEmpty() ) {
				postRepo.delete(posts.get(0));
			}
		}
		// Topic has no posts, removing topic
		topicRepo.delete(topic);
		model.addFlashAttribute("message", "Topic has been removed.");
		return "redirect:/forum/" + topic.getForum().getId();
	}
}