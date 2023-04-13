package com.flab.recipebook.user.domain.dao;

import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.dto.UpdateUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {
    void save(User user);
    void update(User user);
    User findById(Long userNo);
    void deleteById(Long userNo);
    boolean existUserId(String userId);
    boolean existEmail(String email);
}
