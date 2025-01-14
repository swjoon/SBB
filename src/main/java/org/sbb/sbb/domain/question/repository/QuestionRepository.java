package org.sbb.sbb.domain.question.repository;

import org.sbb.sbb.domain.category.entity.Category;
import org.sbb.sbb.domain.question.entity.Question;

import org.sbb.sbb.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Page<Question> findAllByUser(User user, Pageable pageable);

    @Modifying
    @Query("UPDATE Question q "
            +"SET q.viewCount = q.viewCount + 1 "
            +"WHERE q.id = :id")
    void increaseViews(@Param("id") Integer id);


    @Query("SELECT q "
            + "FROM Question q "
            + "LEFT JOIN q.user u1 "
            + "LEFT JOIN q.answerList a1 "
            + "LEFT JOIN a1.user u2 "
            + "WHERE "
            + "q.subject LIKE CONCAT('%', :kw, '%') OR "
            + "q.content LIKE CONCAT('%', :kw, '%') OR "
            + "u1.username LIKE CONCAT('%', :kw, '%') OR "
            + "a1.content LIKE CONCAT('%', :kw, '%') OR "
            + "u2.username LIKE CONCAT('%', :kw, '%') "
            + "ORDER BY q.modifyDate DESC")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    @Query("SELECT q "
            + "FROM Question q "
            + "LEFT JOIN q.user u1 "
            + "LEFT JOIN q.answerList a1 "
            + "LEFT JOIN a1.user u2 "
            + "WHERE "
            + "q.category = :category AND ("
            + "q.subject LIKE CONCAT('%', :kw, '%') OR "
            + "q.content LIKE CONCAT('%', :kw, '%') OR "
            + "u1.username LIKE CONCAT('%', :kw, '%') OR "
            + "a1.content LIKE CONCAT('%', :kw, '%') OR "
            + "u2.username LIKE CONCAT('%', :kw, '%'))"
            + "ORDER BY q.modifyDate DESC")
    Page<Question> findAllByKeywordAndCategory(@Param("kw") String kw, Category category, Pageable pageable);



}
