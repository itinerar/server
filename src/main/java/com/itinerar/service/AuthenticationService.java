package com.itinerar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.itinerar.entity.User;
import com.itinerar.repositories.UserRepository;

@Service
public class AuthenticationService {
	
	private User user;
	
	//public static Long USER_ID = 2L;
	public Long USER_ID;
	
	@Autowired
	UserRepository userRepository;
	
	protected AuthenticationService() {
	}

	public User getUser() {
		user = userRepository.findUserById((long) USER_ID);
		
		
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void setUSER_ID(Long user_id) {
		USER_ID = user_id;
	}
	
	
	
	
	

}
