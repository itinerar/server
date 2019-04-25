package com.itinerar.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "journeys", "post" })
public class CheckPoint {

	private static Logger LOGGER = LoggerFactory.getLogger(CheckPoint.class);
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Double latitude;
	@Column(nullable = false)
	private Double longitude;

	//@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//@JoinColumn(name = "journey_id")
	@ManyToMany(mappedBy = "checkpoints")
	private List<Journey> journeys;

	//@OneToOne(mappedBy = "checkpoint", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@OneToMany(mappedBy = "checkpoint", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Post> posts;
	
	public CheckPoint() {
		journeys = new ArrayList<Journey>();
		posts = new ArrayList<Post>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public List<Post> getPost() {
		return posts;
	}

	public void addPost(Post post) {
		if (post.getCheckpoint() != this) {
			post.setCheckPoint(this);
		}

		if (this.getPost().contains(post)) {
			return;
		}
		this.posts.add(post);
		
	}

	public List<Journey> getJourneys() {
		return this.journeys;
	}

	public void setJourney(Journey journey) {
		
		/*
		if (!journey.getCheckpoints().contains(this)) {
			journey.getCheckpoints().add(this);
		}
		this.journey = journey;
		*/
	}


}
