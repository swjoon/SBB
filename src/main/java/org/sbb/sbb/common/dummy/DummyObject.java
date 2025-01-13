package org.sbb.sbb.common.config.dummy;

import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.category.entity.Category;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.user.entity.User;
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

    protected static Question newQuestion(String subject, String content, User user, Category category) {
        return Question.builder()
                .subject(subject)
                .content(content)
                .user(user)
                .category(category)
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

    protected static Category newCategory(String name) {
        return Category.builder()
                .category(name)
                .build();
    }
}
