package com.cadaverback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cadaverback.dao.UserRepository;
import com.cadaverback.model.dto.UserDTO;
import com.cadaverback.model.jwt.JwtRequest;
import com.cadaverback.model.jwt.JwtResponse;
import com.cadaverback.security.config.JwtTokenUtil;
import com.cadaverback.security.config.UserDetailsImpl;
import com.cadaverback.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class JwtAuthenticationController
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception
    {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token, userDetails.getId()));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception
    {
        com.cadaverback.model.User existingUserOnName = userRepository.findByUsername(user.getUsername()).orElse(null);
        com.cadaverback.model.User existingUserOnEmail = userRepository.findByEmail(user.getEmail()).orElse(null);

        if (null == existingUserOnName && null == existingUserOnEmail)
        {
            return ResponseEntity.ok(userDetailsService.save(user));
        } else if (null != existingUserOnName)
        {
            return new ResponseEntity<>("Username is already taken", HttpStatus.CONFLICT);
        } else
        {
            return new ResponseEntity<>("Email is already taken", HttpStatus.CONFLICT);
        }
    }

    private void authenticate(String username, String password) throws Exception
    {
        try
        {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e)
        {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e)
        {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
