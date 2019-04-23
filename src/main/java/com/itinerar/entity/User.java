package com.itinerar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.dom4j.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.itinerar.teste.Employee;

import io.swagger.annotations.ApiModelProperty;

/*
 * TO DO: cand inserezi friends groups etc si modifici functiile de addXXX sa faci ca aici
 */
@Entity
@Table(name="USER")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler"  , "comments", "journeys" })
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Logger LOGGER = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Size(min = 4, message = "Name should be at least 4 characters")
    private String name;

    @Column(nullable = false)
    @Size(min = 4, message = "Password should be at least 4 characters")
    private String password;

    /* private Journey activeJourney; */

    @Column(nullable = false)
    @Size(min = 3, message = "Email should be at least 3 characters")
    private String email;

    private String address; // TO DO: sa implementezi o adresa buna

    @Past
    @ApiModelProperty(notes = "Birth date should be in the past")
    private Date birthDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<Comment> comments;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    private List<Journey> journeys;

    @OneToOne
    private Journey activeJourney;

    @JsonIgnore
    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="USER_FRIENDSHIPS",
            joinColumns={@JoinColumn(name="USER_ID")},
            inverseJoinColumns={@JoinColumn(name="FRIEND_ID")})
    private List<User> friendRequests = new ArrayList<User>();


    @JsonIgnore
    @ManyToMany(mappedBy="friendRequests")
    private List<User> friends = new ArrayList<User>();

    public User() {
        this.comments = new ArrayList<>();
        this.journeys = new ArrayList<>();
    }

    public User(Long id, @Size(min = 4, message = "Name should be at least 4 characters") String name,
                @Size(min = 4, message = "Password should be at least 4 characters") String password,
                @Size(min = 3, message = "Email should be at least 3 characters") String email, String address,
                @Past Date birthDate, List<Comment> comments, List<Journey> journeys) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.birthDate = birthDate;
        this.comments = comments;
        this.journeys = journeys;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    /*
     * public List<Group> getGroups() { return groups; }
     *
     * public void addGroup(Group group) { this.groups.add(group); }
     */



    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        if (comment.getUser() != this) {
            comment.setUser(this);
        }
        this.comments.add(comment);
    }

    public List<Journey> getJourneys() {
        return journeys;
    }

    public void addJourney(Journey journey) {
        if (journey.getUser() != this) {
            journey.setUser(this);
        }
        if(this.journeys.contains(journey)) {
            return;
        }
        this.journeys.add(journey);
    }

    public void removeJourney(Journey journey) {
        if(!this.journeys.contains(journey)) {
            return;
        }
        this.journeys.remove(journey);
        if (journey.getUser() == this) {
            journey.setUser(null);
        }

    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Journey getActiveJourney() {
        return activeJourney;
    }

    public void setActiveJourney(Journey activeJourney) {
        this.activeJourney = activeJourney;
    }




    public List<User> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<User> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", address="
                + address + ", birthDate=" + birthDate + ", comments=" + comments + ", journeys=" + journeys
                +  "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
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
        User other = (User) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
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
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return true;
    }
}
