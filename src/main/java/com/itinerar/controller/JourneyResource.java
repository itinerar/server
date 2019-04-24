package com.itinerar.controller;

import com.itinerar.entity.CheckPoint;
import com.itinerar.entity.Journey;
import com.itinerar.entity.Post;
import com.itinerar.entity.User;
import com.itinerar.exception.ItinerarEntityNotFoundException;
import com.itinerar.repositories.CheckPointRepository;
import com.itinerar.repositories.JourneyRepository;
import com.itinerar.repositories.PostRepository;
import com.itinerar.repositories.UserRepository;
import com.itinerar.service.AuthenticationService;
import com.itinerar.service.JsonSerializerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
public class JourneyResource {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	JourneyRepository journeyRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired
	CheckPointRepository checkpointRepository;
	
	@Autowired
	AuthenticationService authServices;
	
	@Autowired
	JsonSerializerService myJsonSerialize;
	
	
	// GET
	
	@GetMapping(path = "/copyJourney/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Journey copyJourney(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) {
		Optional<Journey> journey = journeyRepository.findById(id);
		if (!journey.isPresent())
			throw new ItinerarEntityNotFoundException("No journey with id-" + id);
		
		Journey newJourney = new Journey();
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser(); // luam userul curent
		newJourney.setDescription(journey.get().getDescription());
		newJourney.setName(journey.get().getName());
		newJourney.setUser(authUser);	
		newJourney.setParent(journey.get());
		createJourney(authServices.getUser().getId(), newJourney);
		
		return  myJsonSerialize.prepareJourneyforJson(newJourney);
	}
	
	@GetMapping(path = "/journeys", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Journey> retrieveAllCurrentUserJourneys(@RequestHeader(value = "x-user-id") Long userId) {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser(); // luam userul curent
		
		List<Journey> journeysJSON =  journeyRepository.findAllJourneyForUser(authUser.getId());
		return myJsonSerialize.prepareJourneyforJson(journeysJSON);
	}
	
	@GetMapping(path = "/journeys/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Journey> retrieveAllUserJourneys(@PathVariable Long id) throws Exception{
		User user = userRepository.findUserById(id); // luam userul curent
		
		if(user == null) {
			throw new EntityNotFoundException("User with id = " + id + " found");
		}
		
		List<Journey> journeysJSON =  journeyRepository.findAllJourneyForUser(user.getId());
		return myJsonSerialize.prepareJourneyforJson(journeysJSON);
	}
	
	@GetMapping(path = "/journey/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Journey retrieveJourney(@PathVariable Long id) throws Exception{
		
		Optional<Journey> journey = journeyRepository.findById(id);
		
		if (!journey.isPresent())
			throw new ItinerarEntityNotFoundException("No journey with id-" + id);
		
		return myJsonSerialize.prepareJourneyforJson(journey.get());
	}
	
		

	
	
	//@GetMapping(path = "/journey/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Journey retrieveJourneyById(@PathVariable Long id) {
		Optional<Journey> journey =  journeyRepository.findById(id);
		if(!journey.isPresent()) 
			throw new ItinerarEntityNotFoundException("Entity not found with id-"+ id);
		return journey.get();
	}	
	
	//@GetMapping("/journey/{id}/checkpoints")
	public List<CheckPoint> retrieveAllCheckpoints(@PathVariable Long id) {
		Optional<Journey> journeyOptional = journeyRepository.findById(id);
		
		if(!journeyOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Journey with id-" + id);
		}
		
		return journeyOptional.get().getCheckpoints();
	}
	
	//@GetMapping("/journey/{id}/posts")
	public List<Post> retrieveAllPosts(@PathVariable Long id) {
		Optional<Journey> journeyOptional = journeyRepository.findById(id);
		
		if(!journeyOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Journey with id-" + id);
		}
		
		return journeyOptional.get().getPosts();
	}
	
//	@GetMapping("/journey/{id}/user")
	public User retrieveUser(@PathVariable Long id) {
		Optional<Journey> journeyOptional = journeyRepository.findById(id);
		
		if(!journeyOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Journey with id-" + id);
		}
		
		return journeyOptional.get().getUser();
	}
	
	//---------------------------------------------------------------------------------------------------------
	
	// UPDATE, INSERT
	
	@PostMapping(path = "/journey")
	public Journey createJourney(@RequestHeader(value = "x-user-id") Long userId, @Valid @RequestBody Journey journey) {		
		authServices.setUSER_ID(userId);
		User user = authServices.getUser();// preia userul curent logat
		//user.setActiveJourney(journey);
		journey.setUser(user);
		Journey savedJourney = journeyRepository.save(journey);
		
		user.setActiveJourney(savedJourney);
		userRepository.save(user);
		
		
		/*
		 * URI location = ServletUriComponentsBuilder .fromCurrentRequest()
		 * .path("/{id}") .buildAndExpand(savedJourney.getId()) .toUri(); return
		 * ResponseEntity.created(location).build(); // to return a status code back
		 */		
		
		return myJsonSerialize.prepareJourneyforJson(savedJourney);
	}
	
	
	
	//@PostMapping("/journey/{id}/post")
	public ResponseEntity<Object> createPost(@PathVariable Long id, @RequestBody Post post) {
		
		Optional<Journey> journeyOptional = journeyRepository.findById(id);
		
		if(!journeyOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Journey with id-" + id + " not found");
		}

		Journey journey = journeyOptional.get();
		
		post.setJourney(journey);
		
		postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	
	
	//@PostMapping("/journey/{id}/checkpoint")
	public ResponseEntity<Object> createCheckpoint(@PathVariable Long id, @RequestBody CheckPoint checkpoint) {
		
		Optional<Journey> journeyOptional = journeyRepository.findById(id);
		
		if(!journeyOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Journey with id-" + id + " not found");
		}

		Journey journey = journeyOptional.get();
		
		checkpoint.setJourney(journey);
		
		checkpointRepository.save(checkpoint);
		
		URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(checkpoint.getId())
				.toUri();

		return ResponseEntity.created(locationUri).build();
	}
	
	
	//@PostMapping("/journey/{id}/user")
	public ResponseEntity<Object> createUser(@PathVariable Long id, @RequestBody User user) {
		
		Optional<Journey> journeyOptional = journeyRepository.findById(id);
		
		if(!journeyOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Journey with id-" + id + " not found");
		}

		Journey journey = journeyOptional.get();
		
		user.addJourney(journey);
		
		userRepository.save(user);
		
		URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
				.toUri();

		return ResponseEntity.created(locationUri).build();
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	// DELETE Methods
	
	@DeleteMapping(path = "/delete/journey/{id}")
	public void deleteJourneyById(@PathVariable Long id) {
		Optional<Journey> journeyOptional = journeyRepository.findById(id);
		if(!journeyOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Entity journey not found with id-"+ id);		
		}
		journeyRepository.deleteById(id);
	}
	
	@DeleteMapping(path = "/delete/journey")
	public void deleteUser(@PathVariable Journey journey) {
		Optional<Journey> journeyOptional = journeyRepository.findById(journey.getId());
		if(!journeyOptional.isPresent()) {
			throw new ItinerarEntityNotFoundException("Entity not found with id-"+ journey.getId());		
		}
		journeyRepository.delete(journey);
	}
}
