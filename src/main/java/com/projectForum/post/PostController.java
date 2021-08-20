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
import com.projectForum.user.User;

/**
 * This controller will handle the next actions:
 * Find all Posts by id 
 * Delete a post by id
 */

@Controller
@RequestMapping(value = "/post/")
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
	
	/**
	 * This method will toggle on/off whether the user likes the post.
	 * The method will redirect back to the same post. 
	 */
	@GetMapping("like/{postId}")
	public String likePost(@PathVariable int postId, Model model, Authentication authentication) {
		Post post = postServices.findPostById(postId);
		
		// Unregistered and blocked users can not like or remove likes on posts.
		if(authentication == null)
				accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "like/" + postId);
		else if(userServices.isUserBlocked(authentication.getName()))
				accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "like/" + postId);
		
		User user = userServices.findUserByUsername(authentication.getName());
		
		// TODO Add / remove user from the set of those who liked the post
		User found = null;
		for(User u : post.getLikes())
			if (u.getId() == user.getId())
				found = u;
		
		if (found != null)
			post.removeLikeOfUser(found);
		else
			post.addLikeOfUser(found);
		
		// Flag returned of whether the user currently likes the post or not.
		model.addAttribute("liked", found==null);
		return "redirect:/topic/" + post.getTopic().getId() + "#" + post.getId();
	}

	/** This method will give the user the option to edit the post content */
	@GetMapping("edit/{postId}")
	public String editPost(@PathVariable int postId, Model model, Authentication authentication) {
		Post post = postServices.findPostById(postId);
		
		// Unregistered and blocked users can not edit post content.
		if(authentication == null)
				accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "edit/" + postId);
		else if(userServices.isUserBlocked(authentication.getName()))
				accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "edit/" + postId);
		
		// Making sure that user is allowed to edit the post
		if(!authentication.getName().equals(post.getUser().getUsername()) 
				&& !userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "edit/" + postId);
		
		model.addAttribute("editPost", post);
		return "edit_Post_page";
	}
	
	/** This method will edit the original post object and save the new content of the post. */
	@PostMapping("editPost")
	public String editPost(@Valid @ModelAttribute("editPost") Post post, BindingResult bindingResult, Authentication authentication, Model model) {
		
		// Unregistered and blocked users can not edit post content.
		if(authentication == null)
				accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "edit/" + post.getId());
		else if(userServices.isUserBlocked(authentication.getName()))
				accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "edit/" + post.getId());
		
		// Finding the original post
		Post targetPost = postServices.findPostById(post.getId());
		
		if(targetPost == null)
			throw new EntityRequestException("Could not edit post :: " + post.getId());
		
		// Making sure that user is allowed to edit post or is an Admin
		if(!authentication.getName().equals(targetPost.getUser().getUsername())
				&& !userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "editPost/" + post.getId());
		
		// User or Admin allowed to update post
		editServices.updatePost(targetPost, post);
		return "redirect:/topic/" + targetPost.getTopic().getId() + "#" + targetPost.getId();
	}

	/**
	 * This method will return all the posts of a user
	 */
	 @GetMapping("{username}")
	public String getAllPostsByUsername(@PathVariable String username, Model model) {
		
		List<Post> posts = postServices.findPostsByUser(userServices.findUserByUsername(username));
		
		model.addAttribute("posts", posts);
		return "posts";
	}
	
	@GetMapping("delete/{postId}")
	public String deletePost(@PathVariable int postId, Authentication authentication,
								RedirectAttributes model) {
		
		// Unregistered and blocked users can not delete posts.
		if(authentication == null)
				accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "delete/" + postId);
		else if(userServices.isUserBlocked(authentication.getName()))
				accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "delete/" + postId);
		
		// Find post to remove
		Post post = postServices.findPostById(postId);
		
		// Making sure that the post exists
		if(post == null)
			throw new EntityRequestException("Something went wrong, could not reload post :: '" + postId +"'");
		
		// Making sure that the user is allowed to remove it
		else if (!authentication.getName().equals(post.getUser().getUsername()) &&
				!userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "delete/" + post.getId());
		
		deleteService.deletePost(post);
		
		model.addFlashAttribute("message", "post has been removed.");
		return "redirect:/topic/" + post.getTopic().getId();
	}

}