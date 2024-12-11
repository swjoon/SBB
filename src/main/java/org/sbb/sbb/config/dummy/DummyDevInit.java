package org.sbb.sbb.config.dummy;

import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.answer.repository.AnswerRepository;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.question.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInit extends DummyObject{

    @Profile("dev")
    @Bean
    CommandLineRunner init (AnswerRepository answerRepository, QuestionRepository questionRepository) {
        return args -> {
            for(int i  = 1; i <= 300; i++){
                Question q = questionRepository.save(newQuestion("테스트 데이터입니다. [" + i + "]", "테스트 내용입니다.[" + i + "]"));
                Answer a = answerRepository.save(newAnswer("테스트 댓글입니다.[" + i + "]", q));
            }
        };
    }
}
