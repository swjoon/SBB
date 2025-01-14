package org.sbb.sbb.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.common.exception.DataNotFoundException;
import org.sbb.sbb.domain.question.service.QuestionService;
import org.sbb.sbb.domain.user.dto.UserDto;
import org.sbb.sbb.domain.user.entity.User;
import org.sbb.sbb.domain.user.dto.UserDto.UpdatePw;
import org.sbb.sbb.domain.user.dto.UserDto.UserJoin;
import org.sbb.sbb.domain.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final QuestionService questionService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("아이디와 일치하는 유저정보가 없습니다."));
    }

    public void userJoin(UserJoin userJoin) {
        if (userRepository.findByUsername(userJoin.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("이미 등록된 사용자입니다.");
        }

        User user = userJoin.toEntity(bCryptPasswordEncoder.encode(userJoin.getPassword()));
        userRepository.save(user);
    }

    public UserDto.UserProfile getUserProfile(String username ,int page) {
        User user = getUser(username);
        return new UserDto.UserProfile(user,questionService.getQuestionPageByUser(user,page));
    }

    @Transactional
    public void updatePassword(String username, UpdatePw updatePwDto) {
        User user = getUser(username);
        if (!bCryptPasswordEncoder.matches(updatePwDto.getOldPassword(), user.getPassword())) {
            throw new InputMismatchException("기존 비밀번호가 일치하지 않습니다.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(updatePwDto.getNewPassword()));
    }

    @Transactional
    public void setPassword(User user, String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
    }

}
