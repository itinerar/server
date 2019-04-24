package com.itinerar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "comments", "likes", })
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LoggerFactory.getLogger(Post.class);

	@Id
	@GeneratedValue
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
	private Date date;
	
	private String description;

	@Column(nullable = false)
	private String status;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> comments;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Like> likes;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Photo> photos;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "journey_id")
	private Journey journey;

	//@OneToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "checkpoint_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "checkpoint_id")
	private CheckPoint checkpoint;

	public Post() {
		this.photos = new ArrayList<>();
		this.comments = new ArrayList<>();
		this.likes = new ArrayList<>();
		this.status = STATUS.PUBLIC.toString();
	}

	public Post(Long id, List<Comment> comments, Date date, List<Photo> photos, Journey journey, String description) {
		super();
		this.id = id;
		this.comments = comments;
		this.date = date;
		this.photos = photos;
		this.journey = journey;
		this.description = description;
	}

	public List<Like> getLikes() {
		return likes;
	}

	public CheckPoint getCheckpoint() {
		return checkpoint;
	}

	public void setCheckPoint(CheckPoint checkpoint) {
		this.checkpoint = checkpoint;
		if (checkpoint != null ) {
			this.getJourney().addCheckPoint(checkpoint);
		}
	}
	
	// auto increment date
	@PrePersist
    protected void onCreate() {
		date= new Date();
    }

	public String getStatus() {
		return status;
	}
	
	

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void addLike(Like like) {
		if (like.getPost() != this) {
			like.setPost(this);
		}
		this.likes.add(like);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void addComment(Comment comment) {
		if (comment.getPost() != this) {
			comment.setPost(this);
		}
		this.comments.add(comment);
	}

	/*
	 * public Location getLocation() { return location; }
	 * 
	 * public void setLocation(Location location) {
	 * if(!location.getPosts().contains(this)) { location.getPosts().add(this); }
	 * this.location = location; }
	 */

	public Date getDate() {
		return date;
	}

	public void setDate(Date postDate) {
		this.date = postDate;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void addPhoto(Photo photo) {
		if (photo.getPost() != this) {
			photo.setPost(this);
		}
		this.photos.add(photo);
	}

	/*
	 * public List<Like> getLikes() { return likes; }
	 * 
	 * public void addLike(Like like) { this.likes.add(like); }
	 */

	public String getDescription() {
		return description;
	}

	public void setDescription(String postDescription) {
		this.description = postDescription;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", comments=" + comments + /* ", location=" + location + */", postDate=" + date
				+ ", photos=" + photos + ", likes=" + /* likes + */ ", journey=" + journey + ", description="
				+ description + "]";
	}

	public Journey getJourney() {
		return journey;
	}

	public void setJourney(Journey journey) {
		if (!journey.getPosts().contains(this)) {
			journey.getPosts().add(this);
		}
		this.journey = journey;
	}

	public void setJourneyToNull() {
		this.journey = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((journey == null) ? 0 : journey.hashCode());
		result = prime * result + ((likes == null) ? 0 : likes.hashCode());
		result = prime * result + ((photos == null) ? 0 : photos.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (journey == null) {
			if (other.journey != null)
				return false;
		} else if (!journey.equals(other.journey))
			return false;
		if (likes == null) {
			if (other.likes != null)
				return false;
		} else if (!likes.equals(other.likes))
			return false;
		if (photos == null) {
			if (other.photos != null)
				return false;
		} else if (!photos.equals(other.photos))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

}
