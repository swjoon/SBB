package org.sbb.sbb.domain.question.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.common.exception.DataNotFoundException;
import org.sbb.sbb.domain.question.dto.QuestionDto;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.question.repository.QuestionRepository;
import org.sbb.sbb.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question getQuestion(int id) {
        return questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("데이터를 찾을 수 없습니다."));
    }

    public Page<QuestionDto.GetQuestion> getQuestionPage(int page, String kw) {
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
        return questionRepository.findAllByKeyword(kw, pageable).map(QuestionDto.GetQuestion::new);
    }

    public Page<QuestionDto.GetQuestion> getQuestionPageByUser(User user, int page) {
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sort));
        return questionRepository.findAllByUser(user, pageable).map(QuestionDto.GetQuestion::new);
    }

    @Transactional
    public void saveQuestion(QuestionDto.PostQuestion postQuestion, User user) {
        questionRepository.save(postQuestion.toEntity(user));
    }

    @Transactional
    public void modifyQuestion(QuestionDto.PostQuestion postQuestion, Question question) {
        question.setSubject(postQuestion.getSubject().trim());
        question.setContent(postQuestion.getContent().trim());
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    @Transactional
    public void voteQuestion(Question question, User user) {
        if (question.getVoter().contains(user)) {
            question.getVoter().remove(user);
        } else {
            question.getVoter().add(user);
        }
        questionRepository.save(question);
    }

    @Transactional
    public void incrementViewCount(int questionId) {
        questionRepository.increaseViews(questionId);
    }
}
