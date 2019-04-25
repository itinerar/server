package com.itinerar.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "user"})
public class Request {
	private static Logger LOGGER = LoggerFactory.getLogger(Request.class);
	@Id
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	
	/*
	 * @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "friend_id") private User user;
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Friend [id=" + id + ", user=" + user + "]";
	}
}
