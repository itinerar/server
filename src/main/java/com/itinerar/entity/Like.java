package com.itinerar.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LIKES")
public class Like implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LoggerFactory.getLogger(Like.class);

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id")
	private Post post;


	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "photo_id")
	private Photo photo;

	public Like() {
	}

	public Like(Long id, Post post, User user, Photo photo) {
		super();
		this.id = id;
		this.post = post;
		this.photo = photo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Like [id=" + id + ", post=" + post + ", user=" + user + ", photo=" + photo + "]";
	}
}
