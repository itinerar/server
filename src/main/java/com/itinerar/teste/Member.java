package com.itinerar.teste;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "Member")
//@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler"})
public class Member implements Serializable {
	/*
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "fname", nullable = false)
    private String fname;
    @Column(name = "lname", nullable = false)
    private String lname;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requester")
    private Set<Friendship> friendRequests = new HashSet<Friendship>();
    
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "friend")
    private Set<Friendship> friends = new HashSet<Friendship>();
    public Member() {
    }

    public Member(long id, String username, String fname, String lname, Set<Friendship> friendRequests,
			Set<Friendship> friends) {
		super();
		this.id = id;
		this.username = username;
		this.fname = fname;
		this.lname = lname;
		this.friendRequests = friendRequests;
		this.friends = friends;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Set<Friendship> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(Set<Friendship> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public Set<Friendship> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friendship> friends) {
        this.friends = friends;
    }
    */
}