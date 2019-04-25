package com.itinerar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "likes", "post" })
public class Photo implements Serializable{
	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LoggerFactory.getLogger(Photo.class);

	@Id
	@GeneratedValue
	private Long id;

	private String fileName;

	private String fileType;

	@Lob
	@JsonIgnore
	private byte[] data;

	private float latitude;
	private float longitude;

	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id")
	private Post post;
	
	@OneToMany(mappedBy="photo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Like> likes;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id")
	private Location location;

	public Photo() {
		this.location = new Location();
		this.likes = new ArrayList<Like>();
	}
	
	public Photo(Post post, Location location, String fileName, String fileType, byte[] data) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.data = data;
		this.latitude = latitude;
		this.longitude = longitude;
		this.post = post;
		//this.likes = new ArrayList<Like>();
		this.location = location;
	}
	
	
	


	public List<Like> getLikes() {
		return likes;
	}



	public void addLike(Like like) {
		if(like.getPhoto() != this) {
			like.setPhoto(this);
		}
		this.likes.add(like);
	}



	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
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
		if (!post.getPhotos().contains(this)) {
			post.getPhotos().add(this);
		}
		this.post = post;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		if (!location.getPhotos().contains(this)) {
			location.getPhotos().add(this);
		}
		this.location = location;
	}

	/*
	 * public List<Like> getLikes() { return likes; }
	 * 
	 * public void addLike(Like like) { this.likes.add(like); }
	 */

	@Override
	public String toString() {
		return "Photo [id=" + id + ", post=" + post + ", location=" + location + "]";
	}


	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((post == null) ? 0 : post.hashCode());
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
		Photo other = (Photo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		return true;
	}
}
