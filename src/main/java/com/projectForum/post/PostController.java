package com.projectForum.post;

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

import com.projectForum.Services.DeleteService;
import com.projectForum.Services.EditServices;
import com.projectForum.user.UserRepository;

/**
 * This controller will handle the next actions:
 * Find all Posts by id 
 * Delete a post by id
 * */

@Controller
@RequestMapping(value = "/post")
public class PostController {
	
	private PostRepository	postRepo;
	private UserRepository	userRepo;
	private DeleteService	deleteService;
	private EditServices	editServices;	
	
	@Autowired
	public PostController(PostRepository postRepo, UserRepository userRepo, DeleteService deleteService,
			EditServices editServices) {
		this.postRepo = postRepo;
		this.userRepo = userRepo;
		this.deleteService = deleteService;
		this.editServices = editServices;
	}

	// TODO Test those methods
	/** This method will give the user the option to edit the post content*/
	@GetMapping("edit/{postId}")
	public String editPost(@PathVariable int postId, Model model) {
		EditPostForm newEditPost = new EditPostForm();
		newEditPost.setPostId(postId);
		model.addAttribute("editPost", newEditPost);
		return "editPost";
	}
	
	/** This method will edit the original post object and save the new content of the post.*/
	@PostMapping("editPost")
	public String editPost(@Valid @ModelAttribute("newEditPost") EditPostForm newEdit, BindingResult bindingResult, Authentication authentication, Model model) {
		
		// finding original post
		Post post = postRepo.findById(newEdit.getPostId());
		
		// making sure user is allowed to edit post or Admin
		if(authentication.getName().equals(post.getUser().getUsername())
				|| userRepo.findByUsername(authentication.getName()).getRoles().iterator().next().getName().equals("ADMIN")) {
			
			// User or Admin allowed to update post
			editServices.updatePost(post, newEdit);
		
			/* BACKUP BEFORE TESTING
			if(!newEdit.getContent().isEmpty())
				post.setContent(newEdit.getContent());
			
			postRepo.save(post);*/
		}
		// taking user back to the original topic.
		return "redirect:/topic/" + post.getTopic().getId();
	}
	
	/**
	 * This method will return all Users posts*/
	@GetMapping("/{username}")
	public String getAllPostsByUsername(@PathVariable String username, Model model) {
		
		List<Post> posts = postRepo.findPostsByUser(userRepo.findByUsername(username));
		model.addAttribute("posts", posts);
		return "posts";
	}
	
	
	@GetMapping("delete/{postId}")
	public String deletePost(@PathVariable int postId, Authentication authentication,
								RedirectAttributes model) {
		// find post to remove
		Post post = postRepo.findById(postId);
		
		// Making sure that post is exsits and user allowed to remove it or Admin
		if( post != null && authentication != null) {
			if( !authentication.getName().equals(post.getUser().getUsername())
					|| !userRepo.findByUsername(authentication.getName()).getRoles().iterator().next().getName().equals("ADMIN"))
				return "redirect:/";
			else
			{
				// At this point user allowed to remove post
				deleteService.deletePost(post);
				
			}
			
		}
		
		model.addFlashAttribute("message", "post has been removed.");
		return "redirect:/topic/" + post.getTopic().getId();
	}

}