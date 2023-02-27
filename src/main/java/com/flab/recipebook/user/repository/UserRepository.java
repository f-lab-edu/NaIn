package com.flab.recipebook.user.repository;

import com.flab.recipebook.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    User save(User user);


}
