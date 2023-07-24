package com.flab.recipebook.user.domain.dao;

import com.flab.recipebook.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Mapper
public interface UserDao {
    void save(User user);
    void update(User user);
    Optional<User> findById(Long userNo);
    Optional<User> findByUserId(String userId);
    Optional<User> findByRefreshToken(String refreshToken);
    void updateRefreshToken(User user);
    void deleteById(Long userNo);
    boolean existUserId(String userId);
    boolean existEmail(String email);
}
