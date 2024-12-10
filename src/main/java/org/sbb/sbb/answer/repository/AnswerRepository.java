package org.sbb.sbb.answer.repository;

import org.sbb.sbb.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Integer> {
    List<Answer> findAnswersByQuestionId(Integer questionId);
}
