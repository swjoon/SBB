package org.sbb.sbb.board.answer.repository;

import org.sbb.sbb.board.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Integer> {
    List<Answer> findAnswersByQuestionId(Integer questionId);

    Page<Answer> findAnswersByQuestionId(Integer questionId, Pageable pageable);
}
