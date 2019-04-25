package com.itinerar.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "photos", "checkpoints" })
public class Location implements Serializable{
	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LoggerFactory.getLogger(Location.class);
	
	// TO DO: sa pui conditii pe un Location pt atributele sale
	@Id
	@GeneratedValue
	private Long id;
	private String Address;
	private String city;
	private String state;
	private String country;
	private String postalCode;
	
//	@OneToMany(mappedBy="location", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//@JsonManagedReference
	//private List<CheckPoint> checkpoints;
	
	
	@OneToMany(mappedBy="location", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//@JsonManagedReference
	//@JsonIgnore
	private List<Photo> photos;
	
	public Location() {
		this.photos = new ArrayList<Photo>();
		/* this.posts = new ArrayList<Post>(); */
		//this.checkpoints = new ArrayList();
	}

	public Location(Long id, String address, String city, String state, String country, String postalCode,
			List<CheckPoint> checkpoints, /* List<Post> posts, */ List<Photo> photos) {
		super();
		this.id = id;
		Address = address;
		this.city = city;
		this.state = state;
		this.country = country;
		this.postalCode = postalCode;
		//this.checkpoints = checkpoints;
		/* this.posts = posts; */
		this.photos = photos;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public List<Photo> getPhotos() {
		return photos;
	}

	public void addPhoto(Photo photo) {
		if(photo.getLocation() != this) {
			photo.setLocation(this);
		}
		this.photos.add(photo);
	}
	/*
	 * public List<Post> getPosts() { return posts; }
	 * 
	 * 
	 * public void addPost(Post post) { if(post.getLocation() != this) {
	 * post.setLocation(this); } this.posts.add(post); }
	 */
	/*
	 * public List<CheckPoint> getCheckpoints() { return checkpoints; }
	 */


	/*
	 * public void addCheckPoint( CheckPoint checkpoint) {
	 * if(checkpoint.getLocation() != this) { checkpoint.setLocation(this); }
	 * this.checkpoints.add(checkpoint); }
	 * 
	 */


	@Override
	public String toString() {
		return "Location [id=" + id + ", Address=" + Address + ", city=" + city + ", state=" + state + ", country="
				+ country + ", postalCode=" + postalCode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Address == null) ? 0 : Address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		Location other = (Location) obj;
		if (Address == null) {
			if (other.Address != null)
				return false;
		} else if (!Address.equals(other.Address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
}
