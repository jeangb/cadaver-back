package com.cadaverback.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cadaverback.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{

    User findById(final long id);

    Optional<User> findByUsername(String username);

    // Optional<User> findByEmail(String email);
    //
    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsernameAndPassword(String username, String password);
    //
    // Optional<User> findByUsername(String username);
    //
    // boolean existsByUsername(String username);
    //
    // boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
