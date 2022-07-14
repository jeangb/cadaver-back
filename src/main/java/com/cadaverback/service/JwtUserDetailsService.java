package com.cadaverback.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cadaverback.dao.UserRepository;
import com.cadaverback.model.User;
import com.cadaverback.model.dto.UserDTO;
import com.cadaverback.security.config.UserDetailsImpl;

@Service
public class JwtUserDetailsService implements UserDetailsService
{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException
    {
        com.cadaverback.model.User user = userRepository.findByUsername(username).orElse(null);
        if (user == null)
        {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public User save(UserDTO user)
    {
        com.cadaverback.model.User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        return userRepository.save(newUser);
    }

}
