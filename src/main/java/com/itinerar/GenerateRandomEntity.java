package com.itinerar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.fluttercode.datafactory.impl.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;
import com.github.javafaker.Faker;
import com.itinerar.entity.CheckPoint;
import com.itinerar.entity.Comment;
import com.itinerar.entity.Journey;
import com.itinerar.entity.Like;
import com.itinerar.entity.Location;
import com.itinerar.entity.Photo;
import com.itinerar.entity.Post;
import com.itinerar.entity.STATUS;
import com.itinerar.entity.User;
import com.itinerar.repositories.CheckPointRepository;
import com.itinerar.repositories.JourneyRepository;
import com.itinerar.repositories.PostRepository;
import com.itinerar.repositories.UserRepository;
import com.itinerar.service.AuthenticationService;
import com.itinerar.service.DbPhotoStorageService;

@Repository
@Transactional
public class GenerateRandomEntity {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public final int NUMBER_OF_INSTANCES = 10;
	@Autowired
	UserRepository userRepository;
	@Autowired
	JourneyRepository journeyRepository;
	@Autowired
	PostRepository postRepository;

	@Autowired
	CheckPointRepository checkPointRepository;

	@Autowired
	AuthenticationService authService;

	public GenerateRandomEntity() {
		super();
	}

	public List<MultipartFile> getFiles() throws IOException {
		File folder = new File("C:\\Users\\Boyd\\Pictures");
		File[] listOfFiles = folder.listFiles();

		List<MultipartFile> multiList = new ArrayList<MultipartFile>();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				FileInputStream input = new FileInputStream(file);
				MultipartFile multipartFile = new MockMultipartFile("fileItem", file.getName(), "image/png",
						IOUtils.toByteArray(input));
				multiList.add(multipartFile);
			}
		}
		return multiList;
	}

	public void generateRandomDatabase() throws IOException {

		List<MultipartFile> imagesList = getFiles();
		List<User> users = generateUsers();
		List<Journey> journeys = generateJourneys();
		List<Location> locations = generateRandomLocations();
		List<Post> posts = generateRandomEmptyPosts();
		List<Comment> comments = generateRandomComments();
		List<CheckPoint> checkPoints = generateRandomCheckPoints();
		List<Photo> photosList = new ArrayList<Photo>();

		for (MultipartFile photoIndex : imagesList) {
			Photo dbPhoto = new DbPhotoStorageService().createPhotoFromFile(photoIndex);
			photosList.add(dbPhoto);
		}

		// hai sa facem NUMBER_OF_INSTANCES journeys
		for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {

			checkPointRepository.save(checkPoints.get(i));
			comments.get(i).setUser(users.get(i));
			posts.get(i).setJourney(journeys.get(i));

			posts.get(i).setCheckPoint(checkPoints.get(i));
			// checkPoints.get(i).setPost(posts.get(i));
			// posts.get(i).setCheckPoint(checkPoints.get(i));

			posts.get(i).addComment(comments.get(i));
			if (i % 3 == 0) {
				posts.get(i).setStatus(STATUS.PRIVATE.toString());
			} else if (i % 5 == 0) {
				posts.get(i).setStatus(STATUS.PROTECTED.toString());
			}
			// posts.get(i).setUser(users.get(i));

			/* locations.get(i).addPost(posts.get(i)); */
			locations.get(i).addPhoto(photosList.get(i));

			photosList.get(i).setPost(posts.get(i));

			// checkPoints.get(i).setLocation(locations.get(i));
			for (int j = 0; j < NUMBER_OF_INSTANCES; j++) {
				int n = new Random().nextInt(100);
				journeys.get(i).addCheckPoint(checkPoints.get(n));
			}

			users.get(i).addJourney(journeys.get(i));
			// create activeJourney
			Journey defaultActiveJourney = new Journey();
			defaultActiveJourney.setDescription("defaul activeJourney");
			defaultActiveJourney.setName("Default : None");
			defaultActiveJourney.setUser(users.get(i));
			journeyRepository.save(defaultActiveJourney);

			users.get(i).setActiveJourney(defaultActiveJourney);

			// set the authUser
			if (i == 0) {
				authService.USER_ID = users.get(i).getId();
			}
			userRepository.save(users.get(i));
		}

		// Create Friendships and Requests between Users

		users.get(0).getFriendRequests().add(users.get(1));
		users.get(0).getFriendRequests().add(users.get(2));
		users.get(0).getFriendRequests().add(users.get(3));
		users.get(2).getFriendRequests().add(users.get(0)); // 2 will be friend with 0
		users.get(2).getFriendRequests().add(users.get(3));
		users.get(3).getFriendRequests().add(users.get(2)); // 2 will be now friend with 3
		userRepository.save(users.get(0));
		userRepository.save(users.get(2));
		userRepository.save(users.get(3));

		/*
		 * // create activeJourney Journey defaultActiveJourney = new Journey();
		 * defaultActiveJourney.setDescription("defaul journey");
		 * defaultActiveJourney.setName("Default : None");
		 * defaultActiveJourney.setUser(users.get(0));
		 * journeyRepository.save(defaultActiveJourney);
		 * users.get(0).setActiveJourney(defaultActiveJourney);
		 * userRepository.save(users.get(0));
		 */

		// give likes generator
		Like newLike = new Like();
		newLike.setUser(users.get(0));
		posts.get(2).addLike(newLike);
		postRepository.save(posts.get(2));
	}

	public List<CheckPoint> generateRandomCheckPoints() {
		List<CheckPoint> checkPoints = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			CheckPoint checkpoint = new CheckPoint();

			Random rd = new Random();
			checkpoint.setLatitude(rd.nextDouble() + 44);
			checkpoint.setLongitude(rd.nextDouble() + 22);
			checkPoints.add(checkpoint);
		}
		return checkPoints;
	}

	public List<Comment> generateRandomComments() {
		List<Comment> comments = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
			Comment comment = new Comment();
			comment.setDate(generateRandomDate(2012, 2018));
			comment.setMessage("ce postare/poza frumoasa ai, unde ai fost aici?");
			comments.add(comment);
		}
		return comments;
	}

	public List<Location> generateRandomLocations() {
		List<Location> locations = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
			Location location = new Location();
			location.setAddress(generateRandomStreetAddress());
			location.setCity(generateRandomCity());
			location.setCountry("Romania");
			location.setPostalCode(generateRandomPostalCode());
			location.setState("Europa");
			locations.add(location);
		}
		return locations;
	}

	public List<Photo> generateRandomPhotos() {
		List<Photo> photos = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
			Photo photo = new Photo();
			photos.add(photo);
		}
		return photos;
	}

	public List<Post> generateRandomEmptyPosts() {
		List<Post> posts = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
			Post post = new Post();
			post.setDescription("aceasta este postarea mea");
			post.setDate(generateRandomDate(2012, 2018));
			posts.add(post);
		}
		return posts;
	}

	public List<Journey> generateJourneys() {
		List<Journey> journeys = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
			Journey journey = new Journey();
			journey.setDescription("la munte cu familia");
			journey.setName("Paris");
			journeys.add(journey);
		}
		return journeys;
	}

	public List<User> generateUsers() {
		List<User> users = new ArrayList<>();
		for (int i = 1; i <= NUMBER_OF_INSTANCES; i++) {
			User user = new User();
			if (i == 1) {
				user.setAddress(generateRandomStreetAddress());
				user.setBirthDate(generateRandomDate(1990, 2000));
				user.setEmail("abc@gmail.com");
				user.setName("Mihai Eminescu");
				user.setPassword("12345");
				users.add(user);
			} else {
				user.setAddress(generateRandomStreetAddress());
				user.setBirthDate(generateRandomDate(1990, 2000));
				user.setEmail(generateRandomEmail("gmail.com", 4));
				user.setName(generateRandomName());
				user.setPassword(generateRandomString());

				users.add(user);
			}
		}
		return users;
	}

	public String generateRandomEmail(String domain, int length) {
		return RandomStringUtils.random(length, "abcdefghijklmnopqrstuvwxyz") + "@" + domain;
	}

	public String generateRandomStreetAddress() {
		Faker faker = new Faker();
		return faker.address().streetAddress();
	}

	public String generateRandomName() {
		Faker faker = new Faker();
		return faker.name().fullName();
	}

	public String generateRandomPostalCode() {
		Random ran = new Random();
		int x = ran.nextInt(6) + 5;
		return Integer.toString(x);
	}

	public String generateRandomString() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	public Date generateRandomDate(int start, int end) {
		Date randomDate = new Date(ThreadLocalRandom.current().nextLong(start, end));
		return randomDate;
	}

	public String generateRandomCity() {
		DataFactory df = new DataFactory();
		return df.getCity();
	}
}
