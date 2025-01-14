package org.sbb.sbb.domain.mail.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.domain.user.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncMailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendTemporaryPwMail(User user, String pw) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(user.getUsername() + "님의 임시비밀번호 안내 이메일 입니다.");
        message.setTo(user.getEmail());
        message.setText("안녕하세요. 임시비밀번호 안내 관련 메일 입니다." + "[" + user.getUsername() + "]" + "님의 임시 비밀번호는 " + pw + " 입니다.");

        javaMailSender.send(message);
    }
}
