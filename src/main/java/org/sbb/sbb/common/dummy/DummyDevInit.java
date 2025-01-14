package org.sbb.sbb.common.dummy;

import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.answer.repository.AnswerRepository;
import org.sbb.sbb.domain.category.entity.Category;
import org.sbb.sbb.domain.category.repository.CategoryRepository;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.question.repository.QuestionRepository;
import org.sbb.sbb.domain.user.entity.User;
import org.sbb.sbb.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInit extends DummyObject {

    @Profile("dev")
    @Bean
    CommandLineRunner init(UserRepository userRepository, CategoryRepository categoryRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        return args -> {

            Category category1 = categoryRepository.save(newCategory("자유"));
            Category category2 = categoryRepository.save(newCategory("고민"));

            User m = userRepository.save(newUser("jetkid", "성장하는나무", "jetkid1446@naver.com"));

            for (int i = 1; i <= 30; i++) {
                User user = userRepository.save(newUser("testuser" + i, "test" + i, "test" + i + "@gmail.com"));
                Question q = questionRepository.save(newQuestion("테스트 데이터입니다. [" + i + "]", "테스트 내용입니다.[" + i + "]", user, i / 16 == 0 ? category1 : category2));
                Answer a = answerRepository.save(newAnswer("테스트 댓글입니다.[" + i + "]", user, q));
            }
        };
    }
}
