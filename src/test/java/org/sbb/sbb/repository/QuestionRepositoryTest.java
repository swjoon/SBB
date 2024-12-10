package org.sbb.sbb.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.config.dummy.DummyObject;
import org.sbb.sbb.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
public class QuestionRepositoryTest extends DummyObject {

    @Autowired
    private EntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    public void setUp() {
        autoIncrementReset();
        dataSetting();
        em.clear();
    }

    private void autoIncrementReset() {
        em.createNativeQuery("ALTER TABLE question ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    private void dataSetting() {
        Question q1 = DummyObject.newQuestion("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        Question q2 = DummyObject.newQuestion("스프링부트 모델 질문입니다.", "id는 자동으로 생성되나요?");
        questionRepository.save(q1);
        questionRepository.save(q2);
    }

    @Test
    @DisplayName("Question 전체 조회")
    void selectAll() {

        // when
        List<Question> all = questionRepository.findAll();

        // then
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("ID = 1 인 항목 검색")
    void selectOne() {
        // when
        assertEquals(2, questionRepository.count());
        Optional<Question> oq = questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        // then
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("ID = 1 인 항목 수정")
    void testJpa() {
        Optional<Question> oq1 = questionRepository.findById(1);
        assertTrue(oq1.isPresent());
        Question q1 = oq1.get();

        // when
        q1.setSubject("수정된 제목");
        questionRepository.save(q1);
        Optional<Question> oq2 = questionRepository.findById(1);
        assertTrue(oq2.isPresent());
        Question q2 = oq2.get();

        // then
        assertEquals("수정된 제목", q2.getSubject());
    }

    @Test
    @DisplayName("ID = 1 인 항목 삭제")
    void deleteOne() {
        Optional<Question> oq = questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        // when
        questionRepository.delete(q);

        // then
        assertEquals(1, questionRepository.count());
    }
}
