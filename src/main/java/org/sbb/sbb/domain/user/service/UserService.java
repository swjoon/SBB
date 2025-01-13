package org.sbb.sbb.user.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.mail.dto.FindPwDto;
import org.sbb.sbb.mail.service.MailService;
import org.sbb.sbb.user.entity.User;
import org.sbb.sbb.user.dto.req.UpdatePwDto;
import org.sbb.sbb.user.dto.req.UserJoinDto;
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

    private final MailService mailService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("아이디와 일치하는 유저정보가 없습니다."));
    }

    public void userJoin(UserJoinDto userJoinDto) {

        if (userRepository.findByUsername(userJoinDto.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("이미 등록된 사용자입니다.");
        }

        User user = userJoinDto.toEntity(bCryptPasswordEncoder);

        userRepository.save(user);
    }

    @Transactional
    public void sendMail(FindPwDto findPwDto) {
        User user = userRepository.findByUsername(findPwDto.getUsername()).orElseThrow(() -> new NoSuchElementException("아이디와 일치하는 유저정보가 없습니다."));
        if (!user.getEmail().equals(findPwDto.getEmail())) {
            throw new NoSuchElementException("정보가 일치하지 않습니다.");
        }

        String newPw = UUID.randomUUID().toString().substring(0, 8);

        mailService.sendTemporaryPwMail(user, newPw);

        user.setPassword(bCryptPasswordEncoder.encode(newPw));

        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(String username, UpdatePwDto updatePwDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("아이디와 일치하는 유저정보가 없습니다."));
        if (!user.getPassword().equals(bCryptPasswordEncoder.encode(updatePwDto.getNewPassword()))) {
            throw new NoSuchElementException("기존 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(updatePwDto.getNewPassword()));

        userRepository.save(user);
    }
}
