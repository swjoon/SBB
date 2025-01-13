package org.sbb.sbb.board.question.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.board.question.domain.Question;
import org.sbb.sbb.board.question.domain.dto.req.*;
import org.sbb.sbb.board.question.domain.dto.resp.*;
import org.sbb.sbb.board.question.repository.QuestionRepository;
import org.sbb.sbb.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question getQuestion(int id) {
        return questionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("데이터를 찾을 수 없습니다."));
    }

    public Page<GetQuestionDto> getQuestionPage(int page, String kw) {
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
        return questionRepository.findAllByKeyword(kw, pageable);
    }

    @Transactional
    public void saveQuestion(PostQuestionDto postQuestionDto, User user) {
        questionRepository.save(postQuestionDto.toEntity(user));
    }

    @Transactional
    public void modifyQuestion(PostQuestionDto postQuestionDto, Question question) {
        question.setSubject(postQuestionDto.getSubject().trim());
        question.setContent(postQuestionDto.getContent().trim());
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    @Transactional
    public void voteQuestion(Question question, User user) {
        question.getVoter().add(user);
        questionRepository.save(question);
    }
}
