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

import com.projectForum.Exceptions.AccessDeniedRequestException;
import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.Services.DeleteService;
import com.projectForum.Services.EditServices;
import com.projectForum.Services.PostServices;
import com.projectForum.Services.UserServices;

/**
 * This controller will handle the next actions:
 * Find all Posts by id 
 * Delete a post by id
 * */

@Controller
@RequestMapping(value = "/post")
public class PostController {
	
	private PostServices	postServices;
	private UserServices	userServices;
	private DeleteService	deleteService;
	private EditServices	editServices;	
	
	private AccessDeniedRequestException accessDeniedRequestException = new AccessDeniedRequestException();
	private final String localUrl = "/post/";
	
	@Autowired
	public PostController(PostServices postServices, UserServices userServices, DeleteService deleteService,
			EditServices editServices) {
		this.postServices = postServices;
		this.userServices = userServices;
		this.deleteService = deleteService;
		this.editServices = editServices;
	}

	/** This method will give the user the option to edit the post content*/
	@GetMapping("edit/{postId}")
	public String editPost(@PathVariable int postId, Model model, Authentication authentication) {
		Post post = postServices.findPostById(postId);
		
		// Only register user allowed to do acitons
		if(authentication == null)
				accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "edit/" + postId);
		else if(userServices.isUserBlocked(authentication.getName()))
				accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "edit/" + postId);
		
		//	Making sure that user allowed to edit post
		if(!authentication.getName().equals(post.getUser().getUsername()) 
				&& !userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			// User isn't allowed to edit Post
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "edit/" + postId);
		
		//EditPostForm newEditPost = new EditPostForm();
		//newEditPost.setPostId(postId);
		//model.addAttribute("editPost", newEditPost);
		model.addAttribute("editPost", post);
		return "edit_Post_Page";
	}
	
	/** This method will edit the original post object and save the new content of the post.*/
	@PostMapping("editPost")
	public String editPost(@Valid @ModelAttribute("editPost") Post post, BindingResult bindingResult, Authentication authentication, Model model) {
		
		// Only register user allowed to do acitons
		if(authentication == null)
				accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "edit/" + post.getId());
		else if(userServices.isUserBlocked(authentication.getName()))
				accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "edit/" + post.getId());
		
		// finding original post
		Post targetPost = postServices.findPostById(post.getId());
		
		if(targetPost == null)
			throw new EntityRequestException("Could not edit post :: " + post.getId());
		// making sure user is allowed to edit post or Admin
		if(authentication.getName().equals(targetPost.getUser().getUsername())
				&& userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN")) {
			
			// User or Admin allowed to update post
			editServices.updatePost(targetPost, post);
		}
		else
			// User isn't allowed to edit
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "editPost/" + post.getId());
		// taking user back to the original topic.
		return "redirect:/topic/" + targetPost.getTopic().getId();
	}
	
	/**
	 * This method will return all Users posts*/
	@GetMapping("/{username}")
	public String getAllPostsByUsername(@PathVariable String username, Model model) {
		
		List<Post> posts = postServices.findPostsByUser(userServices.findUserByUsername(username));
		
		model.addAttribute("posts", posts);
		return "posts";
	}
	
	
	@GetMapping("delete/{postId}")
	public String deletePost(@PathVariable int postId, Authentication authentication,
								RedirectAttributes model) {
		
		// Only register user allowed to do acitons
		if(authentication == null)
				accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "delete/" + postId);
		else if(userServices.isUserBlocked(authentication.getName()))
				accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "delete/" + postId);
		
		// find post to remove
		Post post = postServices.findPostById(postId);
		
		// Making sure that post is exsits and user allowed to remove it or Admin
		if(post == null)
			throw new EntityRequestException("Something went wrong, could not reload post :: '" + postId +"'");
		
		else if (!authentication.getName().equals(post.getUser().getUsername()) &&
				!userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			// User isn't allowed to delete Post
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "delete/" + post.getId());
		
		//	At this point user allowed to delete post
		deleteService.deletePost(post);
		
		model.addFlashAttribute("message", "post has been removed.");
		return "redirect:/topic/" + post.getTopic().getId();
	}

}