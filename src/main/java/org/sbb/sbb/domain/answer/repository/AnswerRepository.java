package org.sbb.sbb.domain.answer.repository;

import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Integer> {
    Page<Answer> findAll(Pageable pageable);

    List<Answer> findAnswersByQuestion(Question question);

    Page<Answer> findAnswersByQuestion(Question question, Pageable pageable);

    @Query("SELECT a "
            + "FROM Answer a "
            + "LEFT JOIN a.voter as v "
            + "WHERE a.question=:question "
            + "GROUP BY a.id "
            + "ORDER BY count(v) desc, a.createDate desc")
    Page<Answer> findAnswersByVoter(Question question, Pageable pageable);
}
