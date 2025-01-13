package org.sbb.sbb.board.question.repository;

import org.sbb.sbb.board.question.domain.Question;
import org.sbb.sbb.board.question.domain.dto.resp.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer>{

    List<Question> findAll();

    // 검색해서 찾은 question 별 answer의 개수를 가져옴. -> answer 테이블의 question_id 컬럼에 index를 생성해야 효율이 높아진다.
    @Query("SELECT new org.sbb.sbb.board.question.domain.dto.resp.GetQuestionDto(" +
            "q.id, q.subject, q.content, u1.username, u1.nickname, COUNT(DISTINCT a2.id), q.modifyDate) " +
            "FROM Question q " +
            "LEFT JOIN q.user u1 " +
            "LEFT JOIN q.answerList a1 " +
            "LEFT JOIN a1.user u2 " +
            "LEFT JOIN Answer a2 on a2.question = q " +
            "WHERE " +
            "q.subject LIKE CONCAT('%', :kw, '%') OR " +
            "q.content LIKE CONCAT('%', :kw, '%') OR " +
            "u1.username LIKE CONCAT('%', :kw, '%') OR " +
            "a1.content LIKE CONCAT('%', :kw, '%') OR " +
            "u2.username LIKE CONCAT('%', :kw, '%') " +
            "GROUP BY q.id, q.subject, q.content, u1.username, u1.nickname, q.modifyDate ")
    Page<GetQuestionDto> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

}
