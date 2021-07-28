package com.projectForum.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller will handle the next actions:
 * Find all Posts by id 
 * Delete a post by id
 * */

@Controller
@RequestMapping(value = "/post")
public class PostController {
	
	private PostRepository postRepo;
	
	@Autowired
	public PostController(PostRepository postRepository) {
		this.postRepo = postRepository;
	}
	
	@GetMapping("/{username}")
	public String getAllPostsByUsername(@PathVariable String username, Model model) {
		
		// TODO finish this method.
		return "";
	}
}