package com.itinerar.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.itinerar.entity.Comment;
import com.itinerar.entity.Journey;
import com.itinerar.entity.Post;
import com.itinerar.entity.User;
import com.itinerar.exception.EntityAlreadyExistException;
import com.itinerar.exception.ItinerarEntityNotFoundException;
import com.itinerar.repositories.CommentRepository;
import com.itinerar.repositories.JourneyRepository;
import com.itinerar.repositories.PostRepository;
import com.itinerar.repositories.UserRepository;

@RestController
@CrossOrigin
public class CommentResource {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
		@Autowired
		UserRepository userRepository;
		@Autowired
		PostRepository postRepository;
		@Autowired
		CommentRepository commentRepository;
		
		
		// GET
		
		//@GetMapping(path = "/comments", produces=MediaType.APPLICATION_JSON_VALUE)
		public List<Comment> retrieveAllComments() {
			List<Comment> comments =  commentRepository.findAll();
			if(comments == null) {
				throw new ItinerarEntityNotFoundException("No users in table User");
			}
			return comments;
		}
		
		//@GetMapping(path = "/commentResource/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
		public Resource<Comment> retrieveCommentResourceById(@PathVariable Long id) {
			Optional<Comment> comment = commentRepository.findById(id);

			if (!comment.isPresent())
				throw new ItinerarEntityNotFoundException("No journey with id-" + id);
			Resource<Comment> resource = new Resource<Comment>(comment.get());

			ControllerLinkBuilder linkTo = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).retrieveAllComments());

			resource.add(linkTo.withRel("all-comments"));
			return resource;
		}
		
		//@GetMapping(path = "/comment/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
		public Comment retrieveCommentById(@PathVariable Long id) {
			Optional<Comment> comment =  commentRepository.findById(id);
			if(!comment.isPresent()) 
				throw new ItinerarEntityNotFoundException("Entity not found with id-"+ id);
			return comment.get();
		}	
	
		//------------------------------------------------------------------------------
		
		
		//@GetMapping("/comment/{id}/post")
		public Post retrievePost(@PathVariable Long id) {
			Optional<Comment> commentOptional = commentRepository.findById(id);
			
			if(!commentOptional.isPresent()) {
				throw new ItinerarEntityNotFoundException("id-" + id);
			}
			
			return commentOptional.get().getPost();
		}
		
		
	//	@GetMapping("/comment/{id}/user")
		public User retrieveUser(@PathVariable Long id) {
			Optional<Comment> commentOptional = commentRepository.findById(id);
			
			if(!commentOptional.isPresent()) {
				throw new ItinerarEntityNotFoundException("id-" + id);
			}
			
			return commentOptional.get().getUser();
		}
		//-------------------------------------------------------------------------------------------------------------

		// UPDATE, INSERT Method
	//	@PostMapping(path = "/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Object> createComment(@Valid @RequestBody Comment comment) {
			User user = comment.getUser();
			Post post = comment.getPost();
			if(user != null) {
				Optional<User> userOptional = userRepository.findById(user.getId());
				if(userOptional.isPresent()) {
					throw new EntityAlreadyExistException("Entity user with id  = {" + user.getId() + "} already exist, you can't insert a comment with an object that already exist,"
							+ " create empty comment then use /comment/{id}/entity-name to add additional entries");
				}
			}
			
			if(post != null) {
				Optional<Post> postOptional = postRepository.findById(post.getId());
				if(postOptional.isPresent()) {
					throw new EntityAlreadyExistException("Entity user with id  = {" + post.getId() + "} already exist, you can't insert a comment with an object that already exist,"
							+ " create empty comment then use /comment/{id}/entity-name to add additional entries");
				}
			}
			
			Comment savedComment = commentRepository.save(comment);			
			URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}") 
				.buildAndExpand(savedComment.getId())
				.toUri(); 
			return ResponseEntity.created(location).build(); // to return a status code back
			
		}
		
		
		//@PostMapping("/comment/{id}/user")
		public ResponseEntity<Object> createUser(@PathVariable Long id, @RequestBody User user) {
			
			Optional<Comment> commentOptional = commentRepository.findById(id);
			
			if(!commentOptional.isPresent()) {
				throw new ItinerarEntityNotFoundException("User with id-" + id + " not found");
			}

			Comment comment = commentOptional.get();
			
			user.addComment(comment);
			
			userRepository.save(user);
			
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
					.toUri();

			return ResponseEntity.created(location).build();
		}
		
		//@PostMapping("/comment/{id}/post")
		public ResponseEntity<Object> createPost(@PathVariable Long id, @RequestBody Post post) {
			
			Optional<Comment> commentOptional = commentRepository.findById(id);
			
			if(!commentOptional.isPresent()) {
				throw new ItinerarEntityNotFoundException("User with id-" + id + " not found");
			}

			Comment comment = commentOptional.get();
			
			post.addComment(comment);
			
			postRepository.save(post);
			
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
					.toUri();

			return ResponseEntity.created(location).build();
		}
		
		// DELETE Methods
		//@DeleteMapping(path = "/delete/comment/{id}")
		public void deleteCommentById(@PathVariable Long id) {
			Optional<Comment> commentOptional = commentRepository.findById(id);
			if(!commentOptional.isPresent()) {
				throw new ItinerarEntityNotFoundException("Entity not found with id-"+ id);		
			}
			commentRepository.deleteById(id);
		}
		
		//@DeleteMapping(path = "/delete/comment")
		public void deleteComment(@PathVariable Comment comment) {
			Optional<Comment> commentOptional = commentRepository.findById(comment.getId());
			if(!commentOptional.isPresent()) {
				throw new ItinerarEntityNotFoundException("Entity not found with id-"+ comment.getId());		
			}
			commentRepository.delete(comment);
		}
}