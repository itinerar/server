package com.itinerar.teste;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

//@Entity
//@Table(name = "Friendship")
//@IdClass(Friendship.class)
//@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "requester", "friend"})
public class Friendship implements Serializable {
	/*
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Member requester;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Member friend;
	/*
	 * @Temporal(javax.persistence.TemporalType.DATE) private Date date;
	 */
	
	/*
    @Column(nullable = false)
    private boolean active;
    
    
    
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Friendship() {
    }

    public Friendship(Member requester, Member friend, boolean active) {
		super();
		this.requester = requester;
		this.friend = friend;
		this.active = active;
	}

	public Member getRequester() {
        return requester;
    }

    public void setRequester(Member requester) {
    	if(requester == null) {
			this.requester = requester;
		}else if (!requester.getFriendRequests().contains(this)) {
			requester.getFriendRequests().add(this);
		}
		this.requester = requester;
    }

    public Member getFriend() {
        return friend;
    }

    public void setFriend(Member friend) {
    	if(friend == null) {
			this.friend = friend;
		}else if (!friend.getFriends().contains(this)) {
			friend.getFriends().add(this);
		}
		this.friend = friend;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    */
}