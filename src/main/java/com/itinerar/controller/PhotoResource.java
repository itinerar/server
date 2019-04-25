package com.itinerar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.itinerar.controller.response.UploadFileResponse;
import com.itinerar.entity.Like;
import com.itinerar.entity.Photo;
import com.itinerar.entity.Post;
import com.itinerar.entity.User;
import com.itinerar.exception.ItinerarEntityNotFoundException;
import com.itinerar.repositories.LikeRepository;
import com.itinerar.repositories.PhotoRepository;
import com.itinerar.repositories.UserRepository;
import com.itinerar.service.AuthenticationService;
import com.itinerar.service.DbPhotoStorageService;
import com.itinerar.service.JsonSerializerService;
import com.itinerar.teste.DBFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class PhotoResource {

	private static final Logger logger = LoggerFactory.getLogger(PhotoResource.class);
	
	@Autowired
	PhotoRepository photoRepository;
	@Autowired
	LikeRepository likeRepository;
	@Autowired
	AuthenticationService authServices;	
	@Autowired
	JsonSerializerService myJsonSerializer;
//---------------------------------------GET
	@GetMapping("/photo/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
		// Load file from database
		//Photo dbFile = DbPhotoStorageService.getFile(id);

		Optional<Photo> photo = photoRepository.findById(id);
		if (!photo.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		
		Photo dbFile = photo.get();
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(dbFile.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
				.body(new ByteArrayResource(dbFile.getData()));
	}
	
	@GetMapping(path = "/photo/{id}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Like> retrieveLikes(@PathVariable Long id) {
		List<Like> photoLikes = likeRepository.getAllPhotoLikes(id);
		// modific atributul post din raspuns, nu vrea sa fie tot Post pe raspuns
		for(Like like : photoLikes) {
			Photo newPhoto = new Photo();
			newPhoto.setId(like.getPhoto().getId());
			int index = photoLikes.indexOf(like);
			like.setPhoto(newPhoto);
			photoLikes.set(index, like);
		}
		return myJsonSerializer.prepareLikeForJson(photoLikes);
	}
	
	
	@GetMapping(path = "/photo/{id}/numberOfLikes", produces = MediaType.APPLICATION_JSON_VALUE)
	public Long retrievePostNumberOfLikes(@PathVariable Long id) {
		return likeRepository.getNumberOfPhotoLikes(id);
	}
	
	
	
//-------------------------------------------- POST
	
	
	@PostMapping(path = "/photo/{id}/like", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> giveLike(@RequestHeader(value = "x-user-id") Long userId, @PathVariable Long id) throws Exception {
		authServices.setUSER_ID(userId);
		User authUser = authServices.getUser();
		Optional<Photo> photoOptional = photoRepository.findById(id);
		
		if(!photoOptional.isPresent()) {
			throw new EntityNotFoundException("Photo with id = " + id + " don't exist in database");
		}
		
		// mai intai vad daca imaginea nu mai are vreun like de la acest user
		Long numberOfLikes = likeRepository.getNumberOfLikesOfTheUserForThePhoto(authUser.getId(), photoOptional.get().getId());
		if(numberOfLikes >= 1) {
			throw new Exception("The user with id = " + authUser.getId() + " already like the photo with id = " + photoOptional.get().getId());
		}
		// daca nu mai are vreun like
		Like like = new Like();
		like.setUser(authUser);
		photoOptional.get().addLike(like);
		photoRepository.save(photoOptional.get());
				
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}") 
			.buildAndExpand(like.getId())
			.toUri(); 
		return ResponseEntity.created(location).build(); // to return a status code back
		
	}

}
