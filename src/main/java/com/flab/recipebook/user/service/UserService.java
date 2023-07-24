package com.flab.recipebook.user.service;

import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.UserRole;
import com.flab.recipebook.user.domain.dao.UserDao;
import com.flab.recipebook.user.dto.ResponseUserDto;
import com.flab.recipebook.user.dto.SaveUserDto;
import com.flab.recipebook.user.dto.UpdateUserDto;
import com.flab.recipebook.user.exception.DuplicateValueException;
import com.flab.recipebook.user.exception.NotFoundUserException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void save(SaveUserDto saveUserDto) {
        //id 중복검사
        checkUserId(saveUserDto.getUserId());
        //email 중복검사
        checkEmail(saveUserDto.getEmail());
        userDao.save(makeUserFromSaveUserDto(saveUserDto));
    }

    public void updateUser(UpdateUserDto updateUserDto) {
        //현재 패스워드 일치여부
        checkPassword(updateUserDto);
        //이메일 체크
        checkEmail(updateUserDto.getEmail());
        userDao.update(makeUserFromUpdateUserDto(updateUserDto));
    }

    public ResponseUserDto findById(Long userNo){
        User user = vaildateUser(userNo);;
        return convertResponseUserDto(user);
    }

    public void deleteById(Long userNo) {
        userDao.deleteById(userNo);
    }

    public boolean existUserId(String userId){
        return userDao.existUserId(userId);
    }

    public boolean existEmail(String email){
        return userDao.existEmail(email);
    }

    public User makeUserFromSaveUserDto(SaveUserDto saveUserDto){
        return new User(
                saveUserDto.getUserId(),
                passwordEncoder(saveUserDto.getPassword()),
                saveUserDto.getEmail(),
                UserRole.USER
        );
    }

    public User makeUserFromUpdateUserDto(UpdateUserDto updateUserDto){
        String password = updateUserDto.getModifyPassword();

        if (updateUserDto.getModifyPassword() == null) {
            password = updateUserDto.getCurrentPassword();
        }

        return new User(
                updateUserDto.getUserNo(),
                passwordEncoder(password),
                updateUserDto.getEmail()
        );
    }

    public ResponseUserDto convertResponseUserDto(User user){
        return new ResponseUserDto(
                user.getUserNo(),
                user.getUserId(),
                user.getPassword(),
                user.getEmail(),
                user.getUserRole(),
                user.getCreateDate(),
                user.getModifyDate()
        );
    }

    private String passwordEncoder(String password){
        return passwordEncoder.encode(password);
    }

    private void checkUserId(String userId){
        if (existUserId(userId)) {
            throw new DuplicateValueException("아이디가 사용중 입니다.");
        }
    }
    private void checkEmail(String email){
        if (existEmail(email)) {
            throw new DuplicateValueException("이메일이 사용중 입니다.");
        }
    }

    private void checkPassword(UpdateUserDto updateUserDto) {
        User user = vaildateUser(updateUserDto.getUserNo());
        String currentPassword = user.getPassword();

        if (currentPassword.equals(updateUserDto.getCurrentPassword())) {
            throw new IllegalStateException("패스워드가 일치하지 않습니다.");
        }
    }

    private User vaildateUser(Long userNo) {
        return userDao.findById(userNo).orElseThrow(() -> new NotFoundUserException("유저를 찾을 수 없습니다."));
    }
}
