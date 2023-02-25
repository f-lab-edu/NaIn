package com.flab.recipebook.user.dao;

import com.flab.recipebook.user.dto.SaveUserDto;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    public void save(SaveUserDto saveUserDto);
}
