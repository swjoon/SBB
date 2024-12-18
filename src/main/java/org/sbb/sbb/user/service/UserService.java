package org.sbb.sbb.user.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.user.domain.User;
import org.sbb.sbb.user.domain.dto.UserReqDto.*;
import org.sbb.sbb.user.repository.UserRepository;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("아이디와 일치하는 유저정보가 없습니다."));
    }

    public void userJoin(UserJoinReqDto userJoinReqDto) {

        if(userRepository.findByUsername(userJoinReqDto.getUsername()).isPresent()){
            throw new DataIntegrityViolationException("이미 등록된 사용자입니다.");
        }

        User user = userJoinReqDto.toEntity(bCryptPasswordEncoder);

        userRepository.save(user);

        List<Integer> list = new Stack<>();

        Queue<Integer> queue = new ArrayDeque<>();
    }

}
