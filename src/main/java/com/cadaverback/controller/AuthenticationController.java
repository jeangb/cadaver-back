package com.cadaverback.controller;

import java.io.Serializable;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cadaverback.dao.UserRepository;
import com.cadaverback.model.User;

@RestController
public class AuthenticationController {
    private final UserRepository userRepository;

    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(JwtAuthenticationToken jwtAuthenticationToken) {
        final User user = getOrCreateUser(jwtAuthenticationToken);
        return ResponseEntity.ok(new JwtResponse(jwtAuthenticationToken.getToken().getTokenValue(), user.getId()));
    }

    private User getOrCreateUser(Principal principal) {
        final Optional<User> userExist = userRepository.findByUsername(principal.getName());
        final User user;
        if (!userExist.isPresent()) {
            Map<String, Object> tokenAttributes = ((JwtAuthenticationToken) principal).getTokenAttributes();
            User userToSaved = new User(0, principal.getName(), tokenAttributes.get("email").toString());
            user = userRepository.save(userToSaved);
        } else {
            user = userExist.get();
        }
        return user;
    }

    public static class JwtResponse implements Serializable {

        private static final long serialVersionUID = -8091879091924046844L;

        private final String jwttoken;

        private final Long id;

        public JwtResponse(String jwttoken, Long userId) {
            this.jwttoken = jwttoken;
            this.id = userId;
        }

        public String getToken()
        {
            return this.jwttoken;
        }

        public Long getId()
        {
            return id;
        }
    }
}
