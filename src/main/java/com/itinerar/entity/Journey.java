package com.itinerar.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "posts" })
public class Journey implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LoggerFactory.getLogger(Journey.class);

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@Size(min = 4, message = "Journey name should be with at least 4 characters")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	private String description;
	@OneToMany(mappedBy = "journey", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Post> posts;
	
	
	@OneToOne
	private Journey parent;

	//@OneToMany(mappedBy = "journey", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	
	@ManyToMany(cascade = {
	        CascadeType.PERSIST,
	        CascadeType.MERGE
	    })
	    @JoinTable(name = "journey_checkpoint",
	        joinColumns = @JoinColumn(name = "journey_id"),
	        inverseJoinColumns = @JoinColumn(name = "checkpoint_id"))
	private List<CheckPoint> checkpoints;

	public Journey() {
		this.posts = new ArrayList<>();
		this.name = "Default Journey";
		this.checkpoints = new ArrayList<>();
	}

	public Journey(Long id, @Size(min = 4, message = "Journey name should be with at least 4 characters") String name,
                   User user, String description, List<CheckPoint> checkpoints, List<Post> posts) {
		super();
		this.id = id;
		this.name = name;
		this.user = user;
		this.description = description;
		this.checkpoints = checkpoints;
		this.posts = posts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	public Journey getParent() {
		return parent;
	}

	public void setParent(Journey parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		if (user == null) {
			this.user = user;
		} else if (!user.getJourneys().contains(this)) {
			user.getJourneys().add(this);
		}
		this.user = user;
	}

	public void setUserToNull() {
		this.user = null;
	}

	public void removeUser() {
		this.user = null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<CheckPoint> getCheckpoints() {
		return checkpoints;
	}
	
	public List<CheckPoint> setCheckpoints(List<CheckPoint> checkPoints) {
		return this.checkpoints = checkPoints;
	}

	public void addCheckPoint(CheckPoint checkpoint) {
		if(checkpoint == null) return;
		
		for(CheckPoint chkPoint : this.checkpoints) {
			if(chkPoint.getId() == checkpoint.getId()) 
				return;
		}
		
		this.checkpoints.add(checkpoint);
		checkpoint.getJourneys().add(this);
		
	}

	public boolean containsId(final List<CheckPoint> list, final Long id) {
		return list.stream().filter(o -> o.getId().equals(id)).findFirst().isPresent();
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void addPost(Post post) {
		if (post.getJourney() != this) {
			post.setJourney(this);
		}

		if (this.getPosts().contains(post)) {
			return;
		}
		this.posts.add(post);
	}

	@Override
	public String toString() {
		return "Journey [id=" + id + ", name=" + name + ", user=" + user + ", description=" + description + ", posts="
				+ posts + ", checkpoints=" + checkpoints + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkpoints == null) ? 0 : checkpoints.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((posts == null) ? 0 : posts.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Journey other = (Journey) obj;
		if (checkpoints == null) {
			if (other.checkpoints != null)
				return false;
		} else if (!checkpoints.equals(other.checkpoints))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (posts == null) {
			if (other.posts != null)
				return false;
		} else if (!posts.equals(other.posts))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
