package org.sbb.sbb.board.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sbb.sbb.domain.answer.repository.AnswerRepository;
import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.category.entity.Category;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.common.dummy.DummyObject;
import org.sbb.sbb.domain.question.repository.QuestionRepository;
import org.sbb.sbb.domain.user.entity.User;
import org.sbb.sbb.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
public class AnswerRepositoryTest extends DummyObject {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

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
        em.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE answer ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE question ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    private void dataSetting() {
        User user = DummyObject.newUser("testuser1", "test1", "<EMAIL>");
        Category category = DummyObject.newCategory("고민");
        Question q1 = DummyObject.newQuestion("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.", user, category);
        Question q2 = DummyObject.newQuestion("스프링부트 모델 질문입니다.", "id는 자동으로 생성되나요?", user, category);
        userRepository.save(user);
        questionRepository.save(q1);
        questionRepository.save(q2);
        for (int i = 1; i <= 3; i++) {
            Answer answer = DummyObject.newAnswer(i + "", user, q2);
            answerRepository.save(answer);
        }
    }

    @Test
    @DisplayName("select * from answer")
    void selectAll() {
        List<Answer> all = answerRepository.findAll();

        for (Answer a : all) {
            System.out.println(a.toString());
            System.out.println(a.getQuestion().toString());
        }
    }

    @Test
    @DisplayName("select * from answer where id = 1")
    void selectOneById() {
        // when
        Optional<Answer> oa = answerRepository.findById(1);
        assertTrue(oa.isPresent());
        Answer a = oa.get();

        // then
        assertEquals(2, a.getQuestion().getId());
    }

    @Test
    @DisplayName("select * from answer where question_id = 2")
    void getAnswerList() {
        Optional<Question> oq = questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(3, answerList.size());
        assertEquals(1 + "", answerList.get(0).getContent());
    }

    @Test
    @DisplayName("기본정렬 페이징")
    void getAnswerPageDefault() {
        List<Sort.Order> list = new ArrayList<>();
        list.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(0, 2, Sort.by(list));
        Question question = questionRepository.findById(2).get();
        Page<Answer> answerPage = answerRepository.findAnswersByQuestion(question, pageable);

        System.out.println(answerPage.getTotalElements());

        assertEquals(3 + "", answerPage.getContent().get(0).getContent());
        assertEquals(2, answerPage.getContent().size());
    }

    @Test
    @DisplayName("추천수정렬 페이징")
    void getAnswerPageVote() {
        User user1 = userRepository.findById(1L).get();
        User user2 = userRepository.findById(1L).get();
        Answer answer1 = answerRepository.findById(1).get();
        answer1.getVoter().add(user1);
        answer1.getVoter().add(user2);
        Answer answer2 = answerRepository.findById(2).get();
        answer2.getVoter().add(user1);
//        answer2.getVoter().add(user2);

        List<Sort.Order> list = new ArrayList<>();
        list.add(new Sort.Order(Sort.Direction.DESC, "voter"));
        Pageable pageable = PageRequest.of(0, 2, Sort.by(list));
        Question question = questionRepository.findById(2).get();
        Page<Answer> answerPage = answerRepository.findAnswersByQuestion(question, pageable);

        System.out.println(answerPage.getContent().get(0));

        assertEquals(1 + "", answerPage.getContent().get(0).getContent());
    }
}
