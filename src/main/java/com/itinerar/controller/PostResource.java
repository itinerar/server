package com.itinerar.controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.hibernate.mapping.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.itinerar.controller.response.UploadFileResponse;
import com.itinerar.entity.CheckPoint;
import com.itinerar.entity.Comment;
import com.itinerar.entity.Journey;
import com.itinerar.entity.Like;
import com.itinerar.entity.Location;
import com.itinerar.entity.Photo;
import com.itinerar.entity.Post;
import com.itinerar.entity.STATUS;
import com.itinerar.entity.StatusObject;
import com.itinerar.entity.User;
import com.itinerar.exception.EntityAlreadyExistException;
import com.itinerar.exception.ItinerarEntityNotFoundException;
import com.itinerar.repositories.CheckPointRepository;
import com.itinerar.repositories.CommentRepository;
import com.itinerar.repositories.JourneyRepository;
import com.itinerar.repositories.LikeRepository;
import com.itinerar.repositories.PhotoRepository;
import com.itinerar.repositories.UserRepository;
import com.itinerar.service.AuthenticationService;
import com.itinerar.service.DbPhotoStorageService;
import com.itinerar.service.JsonSerializerService;
import com.itinerar.teste.DBFile;
import com.itinerar.repositories.PostRepository;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@RestController
@CrossOrigin
public class PostResource {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	PostRepository postRepository;

	@Autowired
	JourneyRepository journeyRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	PhotoRepository photoRepository;
	@Autowired
	LikeRepository likeRepository;

	@Autowired
	CheckPointRepository checkPointRepository;

	@Autowired
	UserResource userResource;

	@Autowired
	AuthenticationService authServices;

	@Autowired
	JsonSerializerService myJsonSerializer;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(path = "/statuses", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StatusObject> retrieveAllTypesOfStatus() {
		StatusObject stat1 = new StatusObject(STATUS.PRIVATE.toString(), "Only me");
		StatusObject stat2 = new StatusObject(STATUS.PUBLIC.toString(), "Everyone");
		StatusObject stat3 = new StatusObject(STATUS.PROTECTED.toString(), "Friends");
		List<StatusObject> stats = new ArrayList<StatusObject>();
		stats.add(stat1);
		stats.add(stat2);
		stats.add(stat3);
		return stats;
	}



	// GET
	@GetMapping(path = "/databasePosts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Post> retrieveAllDatabasePosts() {
		List<Post> posts = postRepository.giveAllPosts();
		if (posts == null) {
			throw new ItinerarEntityNotFoundException("No posts in table Post");
		}

		return myJsonSerializer.preparePostForJsonObject(posts);
	}

	@GetMapping(path = "/publicPosts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Post> retriveAllPublicPosts() {
		List<Post> allPublicPosts = postRepository.retrieveAllPublicPosts();
		return myJsonSerializer.preparePostForJsonObject(allPublicPosts);
	}

	@GetMapping(path = "/userAndFriendsPosts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Post> retriveUserAndFriendsPosts() {
		// https://www.baeldung.com/java-remove-duplicates-from-list
		List<Post> userPosts = userResource.retrieveAllUserPosts(authServices.getUser().getId());
		List<Post> friendPosts = userResource.retrieveAllFriendsPosts(authServices.getUser().getId());
		return Stream.concat(userPosts.stream(), friendPosts.stream()).collect(Collectors.toList());
	}
	
	@GetMapping(path = "/user/{id}/posts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Post> retrieveCurrentUser(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		User user = userRepository.findUserById(id);
		if(user == null) {
			throw new EntityNotFoundException("no user with id = " + id + " found in database");
		}
		
		if (authUser == null) {
			throw new ItinerarEntityNotFoundException("No user logged in application");
		}
		
		// trebuie sa iau poastarile publice si protected ale acestui utilizator
		List<Post> userPosts = postRepository.findAllUserPosts(id);
		
		return myJsonSerializer.preparePostForJsonObject(userPosts);
		
	}


	// posts imi da ce poate sa vada utilizatorul (postarile lui, ale prietenilor si
	// cele publice)
	@GetMapping(path = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Post> retrievePublicAndUserAndFriendsPosts() {
		// https://www.baeldung.com/java-remove-duplicates-from-list
		List<Post> userAndFriendsPosts = this.retriveUserAndFriendsPosts();
		List<Post> allPublicPosts = this.retriveAllPublicPosts();
		List<Post> concatenate = Stream.concat(userAndFriendsPosts.stream(), allPublicPosts.stream())
				.collect(Collectors.toList());

		Set<Long> idSet = new HashSet<>();
		return concatenate.stream().filter(e -> idSet.add(e.getId())).collect(Collectors.toList());
	}

	@GetMapping(path = "/post/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Comment> retrievePostComments(@PathVariable Long id) {
		List<Comment> postComments = commentRepository.getAllPostComments(id);
		return myJsonSerializer.prepareCommentForJson(postComments);
	}

	@GetMapping(path = "/post/{id}/numberOfComments", produces = MediaType.APPLICATION_JSON_VALUE)
	public Long retrievePostNumberOfComments(@PathVariable Long id) {
		return commentRepository.getNumberOfComments(id);
	}

	@GetMapping(path = "/post/{id}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Like> retrieveLikes(@PathVariable Long id) {
		List<Like> postLikes = likeRepository.getAllPostLikes(id);
		// modific atributul post din raspuns, nu vrea sa fie tot Post pe raspuns
		for (Like like : postLikes) {
			Post newPost = new Post();
			newPost.setId(like.getPost().getId());
			int index = postLikes.indexOf(like);
			like.setPost(newPost);
			postLikes.set(index, like);
		}
		return myJsonSerializer.prepareLikeForJson(postLikes);
	}

	@GetMapping(path = "/post/{id}/numberOfLikes", produces = MediaType.APPLICATION_JSON_VALUE)
	public Long retrievePostNumberOfLikes(@PathVariable Long id) {
		return likeRepository.getNumberOfPostLikes(id);
	}
	
	@GetMapping(path = "/journey/{id}/posts", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Post> retrievePosts(@PathVariable Long id) throws Exception{
		
		Optional<Journey> journey = journeyRepository.findById(id);
		
		if (!journey.isPresent())
			throw new ItinerarEntityNotFoundException("No journey with id-" + id);
		
		List<Post> journeyPosts = postRepository.findAllJourneyPost(id);
		
		return myJsonSerializer.preparePostForJsonObject(journeyPosts);
	}
	
	//@GetMapping(path = "/journeys", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Journey> retrieveAllJourneys() {
		List<Journey> journeys =  journeyRepository.findAll();
		if(journeys == null) {
			throw new ItinerarEntityNotFoundException("No journeys in table Journey");
		}
		return journeys;
	}

	/*
	 * @GetMapping("/post/{id}/journey") public Journey
	 * retrieveJourney(@PathVariable Long id) { Optional<Post> postOptional =
	 * postRepository.findById(id);
	 * 
	 * if (!postOptional.isPresent()) { throw new
	 * ItinerarEntityNotFoundException("No post with id-" + id); } return
	 * postOptional.get().getJourney(); }
	 * 
	 * @GetMapping("/post/{id}/user") public User retrieveUser(@PathVariable Long
	 * id) { Optional<Post> postOptional = postRepository.findById(id);
	 * 
	 * if (!postOptional.isPresent()) { throw new
	 * ItinerarEntityNotFoundException("No post with id-" + id); } return
	 * postOptional.get().getJourney().getUser(); }
	 * 
	 * /*
	 * 
	 * @GetMapping("/post/{id}/location") public Location
	 * retrieveLocation(@PathVariable Long id) { Optional<Post> postOptional =
	 * postRepository.findById(id);
	 * 
	 * if (!postOptional.isPresent()) { throw new
	 * ItinerarEntityNotFoundException("No post with id-" + id); } return
	 * postOptional.get().getLocation(); }
	 */

	// @GetMapping("/post/{id}/photos")
	public List<Photo> retrieveAllPhotos(@PathVariable Long id) {
		Optional<Post> postOptional = postRepository.findById(id);

		if (!postOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("No post with id-" + id);
		}
		return postOptional.get().getPhotos();
	}

	// @GetMapping("/post/{id}/comments")
	public List<Comment> retrieveAllComments(@PathVariable Long id) {
		Optional<Post> postOptional = postRepository.findById(id);

		if (!postOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("No post with id-" + id);
		}
		return postOptional.get().getComments();
	}

	// ---------------------------------------------------------------------------------------------------------

	// GET
	public Post getLastPostFromDatabase() {
		Post post = postRepository.getMaxPost();
		return myJsonSerializer.preparePostForJsonObject(post);
	}
	// UPDATE, INSERT
	@PostMapping(path = "/post")
//	public List<UploadFileResponse> createPost(@Valid @RequestParam("post") String postString,
	public Post createPost(@RequestHeader(value = "x-user-id") Long userId, @Valid @RequestParam("post") String postString, @RequestParam("image") MultipartFile[] file)
			throws Exception {

		// convert Strintg into json object
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(postString));
		reader.setLenient(true);
		Post post = gson.fromJson(reader, Post.class); // deserializes json into target2
		// ------------

		// check Status of the post
		List<String> statuses = new ArrayList<String>();

		for (STATUS status : STATUS.values()) {
			statuses.add(status.name());
		}
		if (!statuses.contains(post.getStatus())) {
			throw new Exception("Status of the POST must be PUBLIC, PRIVATE or PROTECTED");

		}
		// ----------------

		List<UploadFileResponse> responseList = new ArrayList<UploadFileResponse>();
		for (MultipartFile photoIndex : file) { // for each file send in request
			Photo dbPhoto = new DbPhotoStorageService().createPhotoFromFile(photoIndex); // we extract the information
																							// into a Photo object
			post.addPhoto(dbPhoto);// and add it as a photo in Post

			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
					.path(String.valueOf(dbPhoto.getId())).toUriString();
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
					.toUri();
			UploadFileResponse uploadFileResponse = new UploadFileResponse(dbPhoto.getFileName(), fileDownloadUri,
					photoIndex.getContentType(), photoIndex.getSize());
			responseList.add(uploadFileResponse);// here we are preparing the response
		}
		Journey postJourney = post.getJourney();
		// verificam in primul rand daca in obiect exista un journey
		if (postJourney == null) {
			authServices.setUSER_ID(userId);
			User authUser = authServices.getUser(); // luam userul curent
			CheckPoint checkpoint = post.getCheckpoint(); // luam si check-point din postare
			authUser.getActiveJourney().addPost(post); // dat fiind ca postarea nu are un journey, adaugam postarea la
														// active journey-ul existent
			post.setJourney(authUser.getActiveJourney());

			post.setCheckPoint(checkpoint);

			journeyRepository.save(authUser.getActiveJourney());

			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
					.toUri();

			ResponseEntity<Object> responseEntity = ResponseEntity.created(location).build();

			for (UploadFileResponse uploadFileResponse : responseList) {
				uploadFileResponse.setResponseEntity(responseEntity);
			}
			// return responseList;
			post = this.getLastPostFromDatabase();
			
			return myJsonSerializer.preparePostForJsonObject(post);
		}

		/*
		 * // check the checkpoint CheckPoint postCheckpoint = post.getCheckpoint(); if
		 * (postCheckpoint != null) { Optional<CheckPoint> checkPointDatabase =
		 * checkPointRepository.findById(postCheckpoint.getId());
		 * if(checkPointDatabase.isPresent() == true) { postCheckpoint =
		 * checkPointDatabase.get(); postCheckpoint.addPost(post);
		 * checkPointRepository.save(postCheckpoint); }else {
		 * postCheckpoint.addPost(post); checkPointRepository.save(postCheckpoint);
		 * postJourney.addCheckPoint(postCheckpoint); } }
		 */
		Optional<Journey> journeyDatabase = journeyRepository.findById(postJourney.getId());
		if (journeyDatabase.isPresent() == true) { // daca journey exista in baza de date
			CheckPoint checkpoint = post.getCheckpoint();
			postJourney = journeyDatabase.get();
			postJourney.addPost(post);
			postJourney.addCheckPoint(checkpoint);
			journeyRepository.save(postJourney);
		} else {
			throw new Exception(
					"Post journey is not present in Database, please create a journey first then add it to post object");
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();

		ResponseEntity<Object> responseEntity = ResponseEntity.created(location).build();

		for (UploadFileResponse uploadFileResponse : responseList) {
			uploadFileResponse.setResponseEntity(responseEntity);
		}
		// return responseList;
		post = this.getLastPostFromDatabase();
		return myJsonSerializer.preparePostForJsonObject(post);
	}

	@PostMapping(path = "/post/{id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Comment createComment(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id, @Valid @RequestBody Comment comment) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		Optional<Post> postOptional = postRepository.findById(id);

		if (!postOptional.isPresent()) {
			throw new EntityNotFoundException("Post with id = " + id + " don't exist in database");
		}

		comment.setUser(authUser);
		comment.setPost(postOptional.get());
		postOptional.get().addComment(comment);
		Post post = postRepository.save(postOptional.get());
		List<Comment> postComments = post.getComments();
		

		//URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId())
				//.toUri();
		//return ResponseEntity.created(location).build(); // to return a status code back
		return myJsonSerializer.prepareCommentForJson(postComments.get(postComments.size() - 1));

	}

	@PostMapping(path = "/post/{id}/like", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Like giveLike(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) throws Exception {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		Optional<Post> postOptional = postRepository.findById(id);

		if (!postOptional.isPresent()) {
			throw new EntityNotFoundException("Post with id = " + id + " don't exist in database");
		}

		// mai intai vad daca postarea nu mai are vreun like de la acest user
		Long numberOfLikes = likeRepository.getNumberOfLikesOfTheUserForThePost(authUser.getId(),
				postOptional.get().getId());
		if (numberOfLikes >= 1) {
			throw new Exception("The user with id = " + authUser.getId() + "already like the post with id = "
					+ postOptional.get().getId());
		}
		// daca nu mai are vreun like
		Like like = new Like();
		like.setUser(authUser);
		postOptional.get().addLike(like);
		Post post = postRepository.save(postOptional.get());
		List<Like> postLikes = post.getLikes();

		//URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(like.getId())
			//	.toUri();
		//return ResponseEntity.created(location).build(); // to return a status code back
		return myJsonSerializer.prepareLikeForJson(postLikes.get(postLikes.size()-1));

	}

	// DELETE Methods
	@DeleteMapping(path = "/delete/post/{id}")
	public void deletePostById(@PathVariable Long id) {
		Optional<Post> postOptional = postRepository.findById(id);
		if (!postOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Post with id-" + id + " not found");
		}
		postRepository.deleteById(id);
	}

	@DeleteMapping(path = "/delete/post")
	public void deletePost(@PathVariable Post post) {
		Optional<Post> postOptional = postRepository.findById(post.getId());
		if (!postOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + post.getId());
		}
		postRepository.delete(post);
	}

}
