package com.itinerar.controller;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.itinerar.entity.Comment;
import com.itinerar.entity.Journey;
import com.itinerar.entity.Post;
import com.itinerar.entity.User;
import com.itinerar.exception.ItinerarEntityNotFoundException;
import com.itinerar.repositories.CommentRepository;
import com.itinerar.repositories.JourneyRepository;
import com.itinerar.repositories.PostRepository;
import com.itinerar.repositories.UserRepository;
import com.itinerar.service.AuthenticationService;
import com.itinerar.service.JsonSerializerService;
import com.itinerar.teste.Employee;
import com.itinerar.teste.EmployeeRepository;

@RestController
@CrossOrigin
public class UserResource {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UserRepository userRepository;

	@Autowired
	JourneyRepository journeyRepository;
	@Autowired
	PostRepository postRepository;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	AuthenticationService authServices;
	@Autowired
	JsonSerializerService myJsonSerializer;

	// GET
	
	@GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public User retrieveCurrentUser(@RequestHeader(value = "x-user-id") Long userId) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		
		
		if (authUser == null) {
			throw new ItinerarEntityNotFoundException("No user logged in application");
		}
		return myJsonSerializer.prepareUserForJson(authUser);
	}
	
	@GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public User retrieveUser(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		User user = userRepository.findUserById(id);
		if(user == null) {
			throw new EntityNotFoundException("no user with id = " + id + " found in database");
		}
		
		if (authUser == null) {
			throw new ItinerarEntityNotFoundException("No user logged in application");
		}
		return myJsonSerializer.prepareUserForJson(user);
	}
	
	
	@GetMapping(path = "/user/{id}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> retrieveFriendsForUser(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		User user = userRepository.findUserById(id);
		if(user == null) {
			throw new EntityNotFoundException("no user with id = " + id + " found in database");
		}
		
		if (authUser == null) {
			throw new ItinerarEntityNotFoundException("No user logged in application");
		}
		
		List<User> friends = userRepository.findFriends(id);
		return myJsonSerializer.prepareUserForJson(friends);
	}
	
	

	//@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> retrieveAllUsers() {
		List<User> users = userRepository.findAll();
		if (users == null) {
			throw new ItinerarEntityNotFoundException("No users in table User");
		}
		return users;
	}

	//@GetMapping(path = "/userResource/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Resource<User> retrieveUserResourceById(@PathVariable Long id) {
		User user = userRepository.findUserById(id);
		if (user == null)
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		Resource<User> resource = new Resource<User>(user);
		ControllerLinkBuilder linkTo = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());

		resource.add(linkTo.withRel("all-users"));

		return resource;
	}

	//@GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public User retrieveUserById(@PathVariable Long id) {
		User user = userRepository.findUserById(id);
		if (user == null)
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		return user;
	}

	// ------------------------------------------------------------------------------

	//@GetMapping("/user/{id}/journeys")
	public List<Journey> retrieveAllJourneys(@PathVariable Long id) {
		Optional<User> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("id-" + id);
		}

		return userOptional.get().getJourneys();
	}

	//@GetMapping("/user/{id}/comments")
	public List<Comment> retrieveAllComments(@PathVariable Long id) {
		Optional<User> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("id-" + id);
		}

		return userOptional.get().getComments();
	}
	
	
	// USER POSTS AND USER friends posts
	
	@GetMapping(path = "/userPosts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Post> retrieveAllUserPosts(@RequestHeader(value = "x-user-id") Long userId) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		List<Post> userPosts = postRepository.findAllUserPosts(authUser.getId());
		return myJsonSerializer.preparePostForJsonObject(userPosts);
	}
	
	@GetMapping(path = "/friends/posts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Post> retrieveAllFriendsPosts(@RequestHeader(value = "x-user-id") Long userId) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		List<Post> userPosts = postRepository.findAllUserFriendsPosts(authUser.getId());
		return myJsonSerializer.preparePostForJsonObject(userPosts);
	}

	// FRIEND REQUESTS AND FRIENDS

	@GetMapping(path = "/userSentRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	private List<User> userSentRequest(@RequestHeader(value = "x-user-id") Long userId) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		List<User> userRequests = userRepository.findSentRequests(authUser.getId());
		
		return myJsonSerializer.prepareUserForJson(userRequests);
	}

	@GetMapping(path = "/userReceivedRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	private List<User> userReceivedRequest(@RequestHeader(value = "x-user-id") Long userId) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		List<User> userReceivedRequests =  userRepository.findReceivedRequests(authUser.getId());
		return myJsonSerializer.prepareUserForJson(userReceivedRequests);
	}

	@GetMapping(path = "/userFriends", produces = MediaType.APPLICATION_JSON_VALUE)
	private List<User> findFriends(@RequestHeader(value = "x-user-id") Long userId) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		List<User> friends =  userRepository.findFriends(authUser.getId());
		return myJsonSerializer.prepareUserForJson(friends);
	}

	// -------------------------------------------------------------------------------------------------------------

	// UPDATE, INSERT Method
	//@PostMapping(path = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build(); // to return a status code back

	}

	//@PostMapping("/user/{id}/journey")
	public ResponseEntity<Object> createJourney(@PathVariable Long id, @RequestBody Journey journey) {

		Optional<User> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("User with id-" + id + " not found");
		}

		User user = userOptional.get();

		journey.setUser(user);

		journeyRepository.save(journey);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(journey.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	//@PostMapping("/user/{id}/comment")
	public ResponseEntity<Object> createComment(@PathVariable Long id, @RequestBody Comment comment) {

		Optional<User> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("User with id-" + id + " not found");
		}

		User user = userOptional.get();

		comment.setUser(user);

		commentRepository.save(comment);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	
	
	// FRIENDS POSTS
	
	@PostMapping("/requestFriendship/{id}")
	public ResponseEntity<Object> requestFriendShip(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) throws Exception {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		Long userLoggedId = authUser.getId();
		Optional<User> optionalUser = userRepository.findById((long) userLoggedId);
		Optional<User> optionalNewFriend = userRepository.findById((long) id);
		if (!optionalUser.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + userLoggedId);
		else if (!optionalNewFriend.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		
		List<User> userFriends = findFriends(authServices.getUser().getId()); // find friends for current user
		if(userFriends.contains(optionalNewFriend.get())) {
			throw new Exception("Entity with id = " + userLoggedId + " is already friend with id = " + id);
		}
		
		
		optionalUser.get().getFriendRequests().add(optionalNewFriend.get());
		userRepository.save(optionalUser.get());
				
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalUser.get().getName())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	
	
	@PostMapping("/cancelRequest/{id}")
	public ResponseEntity<Object> cancelRequest(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) throws Exception {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		Long userLoggedId = authUser.getId();
		Optional<User> optionalUser = userRepository.findById((long) userLoggedId);
		Optional<User> optionalNewFriend = userRepository.findById((long) id);
		if (!optionalUser.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + userLoggedId);
		else if (!optionalNewFriend.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		
		List<User> userRequests = userReceivedRequest(authServices.getUser().getId()); // find friendship requests for this user
		if(!userRequests.contains(optionalNewFriend.get())) {
			throw new Exception("Entity with id = " + userLoggedId + " don't have a friend request from = " + id);
		}
		
		optionalNewFriend.get().getFriendRequests().remove(optionalUser.get());
		userRepository.save(optionalNewFriend.get());
				
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalNewFriend.get().getName())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	
	@PostMapping("/acceptFriendShip/{id}")
	public ResponseEntity<Object> acceptFriendship(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) throws Exception {	
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		Long userLoggedId = authUser.getId();
		Optional<User> optionalUser = userRepository.findById((long) userLoggedId);
		Optional<User> optionalNewFriend = userRepository.findById((long) id);
		if (!optionalUser.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + userLoggedId);
		else if (!optionalNewFriend.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		
		List<User> userFriends = findFriends(authServices.getUser().getId()); // find friends for current user
		List<User> userRequests = userReceivedRequest(authServices.getUser().getId()); // find friends for current user
		if(userFriends.contains(optionalNewFriend.get())) {
			throw new Exception("Entity with id = " + userLoggedId + " is already friend with id = " + id);
		}
		
		if(!userRequests.contains(optionalNewFriend.get())) {
			throw new Exception("Entity with id = " + userLoggedId + " don't have a friend request from = " + id);
		}
		optionalUser.get().getFriendRequests().add(optionalNewFriend.get());
		userRepository.save(optionalUser.get());
				
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalUser.get().getName())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	// DELETE Methods
	@DeleteMapping(path = "/delete/user/{id}")
	public void deleteUserById(@PathVariable Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		}
		userRepository.deleteById(id);
	}

	@DeleteMapping(path = "/delete/user")
	public void deleteUser(@PathVariable User user) {
		Optional<User> userOptional = userRepository.findById(user.getId());
		if (!userOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + user.getId());
		}
		userRepository.delete(user);
	}
	
	
	@DeleteMapping(path = "/removeFriendShip/{id}")
	public void removeFriend(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		Long userLoggedId = authUser.getId();
		User user = userRepository.findById((long) userLoggedId).get();
		User enemy = userRepository.findById((long) id).get();		
		user.getFriendRequests().remove(enemy);
		user.getFriends().remove(enemy);
		
		enemy.getFriendRequests().remove(user);
		enemy.getFriends().remove(user);
		userRepository.save(user);
		userRepository.save(enemy);
	}
}