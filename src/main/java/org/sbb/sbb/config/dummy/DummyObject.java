package org.sbb.sbb.config.dummy;

import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.user.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    protected static User newUser(String name, String nickname, String email) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        return User.builder()
                .username(name)
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname(nickname).email(email)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .build();
    }

    protected static Question newQuestion(String subject, String content, User user) {
        return Question.builder()
                .subject(subject)
                .content(content)
                .user(user)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .build();
    }

    protected static Answer newAnswer(String content, User user, Question question) {
        return Answer.builder()
                .content(content)
                .user(user)
                .question(question)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .build();
    }
}
