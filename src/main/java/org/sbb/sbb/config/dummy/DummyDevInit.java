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
            Question q1 = questionRepository.save(newQuestion("test1", "test1 Content"));
            Question q2 = questionRepository.save(newQuestion("test2", "test2 Content"));
            Question q3 = questionRepository.save(newQuestion("test3", "test3 Content"));
            Question q4 = questionRepository.save(newQuestion("test4", "test4 Content"));
            Question q5 = questionRepository.save(newQuestion("test5", "test5 Content"));

            Answer a1 = answerRepository.save(newAnswer("test1 Answer", q1));
            Answer a2 = answerRepository.save(newAnswer("test2 Answer", q1));
            Answer a3 = answerRepository.save(newAnswer("test3 Answer", q2));
            Answer a4 = answerRepository.save(newAnswer("test4 Answer", q2));
            Answer a5 = answerRepository.save(newAnswer("test5 Answer", q5));
        };
    }
}
