package com.itinerar.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*@Entity*/
public class Group {
	private static Logger LOGGER = LoggerFactory.getLogger(Group.class);
	/*
	 * @Id
	 * 
	 * @GeneratedValue private Long id;
	 * 
	 * @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "user_id")
	 * 
	 * @JsonBackReference private User user;
	 * 
	 * protected Group() { }
	 * 
	 * public Long getId() { return id; }
	 * 
	 * public void setId(Long id) { this.id = id; }
	 * 
	 * public User getUser() { return user; }
	 * 
	 * public void setUser(User user) { this.user = user; }
	 * 
	 * @Override public String toString() { return "Group [id=" + id + ", user=" +
	 * user + "]"; }
	 * 
	 * @Override public int hashCode() { final int prime = 31; int result = 1;
	 * result = prime * result + ((id == null) ? 0 : id.hashCode()); result = prime
	 * * result + ((user == null) ? 0 : user.hashCode()); return result; }
	 * 
	 * @Override public boolean equals(Object obj) { if (this == obj) return true;
	 * if (obj == null) return false; if (getClass() != obj.getClass()) return
	 * false; Group other = (Group) obj; if (id == null) { if (other.id != null)
	 * return false; } else if (!id.equals(other.id)) return false; if (user ==
	 * null) { if (other.user != null) return false; } else if
	 * (!user.equals(other.user)) return false; return true; }
	 */
}
