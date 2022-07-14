package com.cadaverback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cadaverback.dao.UserRepository;
import com.cadaverback.model.Status;
import com.cadaverback.model.User;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController
{

    @Autowired
    UserRepository userRepository;

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser)
    {
        User user = userRepository.findByUsernameOrEmail(newUser.getUsername(), newUser.getEmail()).orElse(null);

        if (null == user)
        {
            userRepository.save(newUser);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/users/login")
    public ResponseEntity<User> loginUser(@RequestBody User logginUser)
    {
        User user = userRepository.findByUsernameAndPassword(logginUser.getUsername(), logginUser.getPassword()).orElse(null);

        if (null == user)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // user.setLoggedIn(true);
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<String> logUserOut(@RequestBody User logginUser)
    {

        User user = userRepository.findByUsernameOrEmail(logginUser.getUsername(), logginUser.getEmail()).orElse(null);

        if (null == user)
        {
            return new ResponseEntity<>("Can't logout user.", HttpStatus.BAD_REQUEST);
        }

        // user.setLoggedIn(false);
        userRepository.save(user);
        return new ResponseEntity<>("User logged-out successfully!.", HttpStatus.OK);
    }

    @DeleteMapping("/users/all")
    public Status deleteUsers()
    {
        userRepository.deleteAll();
        return Status.SUCCESS;
    }
}
