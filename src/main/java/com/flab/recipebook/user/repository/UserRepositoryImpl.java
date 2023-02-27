package com.flab.recipebook.user.repository;

import com.flab.recipebook.user.domain.User;

import java.util.HashMap;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private static HashMap<Long, User> userMap = new HashMap<>();
    private static Long seq = 1L;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userMap.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User save(User user) {
        user.setId(++seq);
        return userMap.put(user.getId(), user);
    }
}
