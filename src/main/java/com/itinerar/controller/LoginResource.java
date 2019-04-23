package com.itinerar.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itinerar.entity.Comment;
import com.itinerar.entity.User;
import com.itinerar.repositories.UserRepository;
import com.itinerar.service.JsonSerializerService;

@RestController
@CrossOrigin
public class LoginResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    JsonSerializerService myJsonSerialize;

    @PostMapping(path = "/login")
    public User retriveUserLogin(@Valid @RequestParam("email") String email , @Valid @RequestParam("password")  String password) {
        User user = userRepository.findUserByEmail(email);
        if(user == null) {
            throw new EntityNotFoundException("no user with email  = " + email + "and password = " + password);
        }

        return myJsonSerialize.prepareUserForJson(user);
    }
}
