package org.sbb.sbb.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sbb.sbb.answer.repository.AnswerRepository;
import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.config.dummy.DummyObject;
import org.sbb.sbb.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
public class AnswerRepositoryTest extends DummyObject {

    @Autowired
    private EntityManager em;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        autoIncrementReset();
        dataSetting();
        em.clear();
    }

    private void autoIncrementReset() {
        em.createNativeQuery("ALTER TABLE answer ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE question ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    private void dataSetting() {
        Question q1 = DummyObject.newQuestion("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        Question q2 = DummyObject.newQuestion("스프링부트 모델 질문입니다.", "id는 자동으로 생성되나요?");

        Answer a1 = DummyObject.newAnswer("네 자동으로 생성됩니다.",q2);

        questionRepository.save(q1);
        questionRepository.save(q2);

        answerRepository.save(a1);
    }

    @Test
    @DisplayName("select * from answer")
    void selectAll(){
        List<Answer> all = answerRepository.findAll();

        for(Answer a : all){
            System.out.println(a.toString());
            System.out.println(a.getQuestion().toString());
        }
    }

    @Test
    @DisplayName("select * from answer where id = 1")
    void selectOneById(){
        // when
        Optional<Answer> oa = answerRepository.findById(1);
        assertTrue(oa.isPresent());
        Answer a = oa.get();

        // then
        assertEquals(2, a.getQuestion().getId());
    }

    @Test
    @DisplayName("select * from answer where question_id = 2")
    void getAnswerList(){
        Optional<Question> oq = questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }
}
