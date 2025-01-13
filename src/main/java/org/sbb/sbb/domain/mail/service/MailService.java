package org.sbb.sbb.domain.mail.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.domain.mail.dto.MailDto.FindPw;
import org.sbb.sbb.domain.user.entity.User;
import org.sbb.sbb.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InputMismatchException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final UserService userService;
    private final AsyncMailService asyncMailService;

    @Transactional
    public void sendMail(FindPw findPw) {
        User user = userService.getUser(findPw.getUsername());
        if (!user.getEmail().equals(findPw.getEmail())) {
            throw new InputMismatchException("정보가 일치하지 않습니다.");
        }

        String newPw = UUID.randomUUID().toString().substring(0, 8);

        asyncMailService.sendTemporaryPwMail(user, newPw);

        userService.setPassword(user, newPw);
    }

}
