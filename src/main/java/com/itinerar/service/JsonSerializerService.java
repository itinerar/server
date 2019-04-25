package com.itinerar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import com.itinerar.entity.CheckPoint;
import com.itinerar.entity.Comment;
import com.itinerar.entity.Journey;
import com.itinerar.entity.Like;
import com.itinerar.entity.Photo;
import com.itinerar.entity.Post;
import com.itinerar.entity.User;

@Service
public class JsonSerializerService {
	public JsonSerializerService() {
	}

	public Post preparePostForJsonObject(Post post) {
		CheckPoint checkpoint = (CheckPoint) Hibernate.unproxy(post.getCheckpoint());
		Journey journey = (Journey) Hibernate.unproxy(post.getJourney());
		User userJourney = (User) Hibernate.unproxy(journey.getUser());
		Journey temp = (Journey) Hibernate.unproxy(userJourney.getActiveJourney());

		// realizez o postare noua
		Post postare = new Post();
		postare.setId(post.getId());
		postare.setDescription(post.getDescription());
		postare.setDate(post.getDate());
		for (Photo photo : post.getPhotos()) {
			postare.getPhotos().add(photo);
			// postare.addPhoto(photo);
		}
		postare.setJourneyToNull();

		Journey jsonJourney = prepareJourneyforJson(journey);
		postare.setJourney(jsonJourney);

		// setez checkpoint pt postare
		if (checkpoint != null) {
			CheckPoint newCheckPoint = prepareCheckPointforJson(checkpoint);
			postare.setCheckPoint(newCheckPoint);

		}
		List<Comment> jsonComments = new ArrayList<Comment>();
		for(Comment comment : post.getComments()) {
			Comment newComment = new Comment();
			newComment.setId(comment.getId());
			newComment.setMessage(comment.getMessage());
			newComment.setUser(prepareUserForJson(comment.getUser()));
			newComment.setDate(comment.getDate());
			jsonComments.add(newComment);
		}
		
		postare.setComments(jsonComments);
		
		
		
		return postare;
	}

	
	public CheckPoint prepareCheckPointforJson(CheckPoint checkpoint) {
		if(checkpoint != null) {
			CheckPoint newCheckPoint = new CheckPoint();
			newCheckPoint.setId(checkpoint.getId());
			newCheckPoint.setLatitude(checkpoint.getLatitude());
			newCheckPoint.setLongitude(checkpoint.getLongitude());
		}
		return checkpoint;
	}
	public Journey prepareJourneyforJson(Journey journey) {
		User userJourney = (User) Hibernate.unproxy(journey.getUser());
		Journey temp = (Journey) Hibernate.unproxy(userJourney.getActiveJourney());

		Journey calatorie = new Journey();
		calatorie.setId(journey.getId());
		calatorie.setName(journey.getName());
		calatorie.setDescription(journey.getDescription());
		calatorie.setUserToNull();
		calatorie.setCheckpoints(journey.getCheckpoints());
		if(journey.getParent() != null) {
			Journey journeyParent = new Journey();
			journeyParent.setId(journey.getParent().getId());
			if(journey.getParent().getParent() != null) {
				Journey journeyParentUP = new Journey();
				journeyParentUP.setId(journey.getParent().getParent().getId());
				journeyParent.setParent(journeyParentUP);
			}
			
			calatorie.setParent(journeyParent);
		}

		// creez un nou user, dar care sa aibe activeJourney = null
		User user = new User();
		user.setId(userJourney.getId());
		user.setName(userJourney.getName());
		user.setPassword(userJourney.getPassword());
		user.setEmail(userJourney.getEmail());
		user.setBirthDate(userJourney.getBirthDate());
		user.setAddress(userJourney.getAddress());
		Journey tempActiveJourney = new Journey();
		tempActiveJourney.setId(temp.getId());
		tempActiveJourney.setName(temp.getName());
		user.setActiveJourney(tempActiveJourney);

		calatorie.setUser(user);

		return calatorie;
	}

	public List<Journey> prepareJourneyforJson(List<Journey> journeys) {
		for (Journey journey : journeys) {
			Journey newJourney = this.prepareJourneyforJson(journey);
			int index = journeys.indexOf(journey);
			journeys.set(index, newJourney);
		}
		return journeys;
	}
	public List<Post> preparePostForJsonObject(List<Post> posts) {
		for (Post post : posts) {
			Post newPost = this.preparePostForJsonObject(post);
			int index = posts.indexOf(post);
			posts.set(index, newPost);
		}
		return posts;
	}

	public User prepareUserForJson(User user) {
		Journey activeJourney = (Journey) Hibernate.unproxy(user.getActiveJourney());
		User newUser = new User();
		newUser.setId(user.getId());
		newUser.setAddress(user.getAddress());
		newUser.setBirthDate(user.getBirthDate());
		newUser.setEmail(user.getEmail());
		newUser.setName(user.getName());
		newUser.setPassword(user.getPassword());

		Journey tempActiveJourney = new Journey();
		tempActiveJourney.setId(activeJourney.getId());
		newUser.setActiveJourney(tempActiveJourney);

		return newUser;
	}

	public List<User> prepareUserForJson(List<User> users) {
		for (User user : users) {
			User newUser = this.prepareUserForJson(user);
			int index = users.indexOf(user);
			users.set(index, newUser);
		}
		return users;
	}

	public Comment prepareCommentForJson(Comment comment) {
		Comment newComment = new Comment();
		newComment.setId(comment.getId());
		newComment.setMessage(comment.getMessage());
		newComment.setDate(comment.getDate());
		newComment.setUser(prepareUserForJson(comment.getUser()));
		newComment.setPost(preparePostForJsonObject(comment.getPost()));
		return newComment;
	}

	public List<Comment> prepareCommentForJson(List<Comment> comments) {
		for (Comment comment : comments) {
			Comment newComment = this.prepareCommentForJson(comment);
			int index = comments.indexOf(comment);
			comments.set(index, newComment);
		}
		return comments;
	}

	public Like prepareLikeForJson(Like like) {
		Post likePost = null;
		User likeUser = null;
		if (like.getPost() != null) {
			likePost = like.getPost();
			// cel de jos nu merge pt ca eu pe json vreau doar sa returnez id-ul postului
			// likePost = preparePostForJsonObject(like.getPost());
		}

		if (like.getUser() != null) {
			likeUser = prepareUserForJson(like.getUser());
		}

		Like newLike = new Like();
		newLike.setId(like.getId());
		newLike.setPhoto(like.getPhoto());
		newLike.setPost(likePost);
		newLike.setUser(likeUser);

		return newLike;
	}

	public List<Like> prepareLikeForJson(List<Like> likes) {
		for (Like like : likes) {
			Like newLike = this.prepareLikeForJson(like);
			int index = likes.indexOf(like);
			likes.set(index, newLike);
		}
		return likes;
	}
}
