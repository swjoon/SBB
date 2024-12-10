package org.sbb.sbb.question.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.answer.domain.dto.AnswerRespDto.*;
import org.sbb.sbb.answer.repository.AnswerRepository;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.question.domain.dto.QuestionRespDto.*;
import org.sbb.sbb.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public QuestionContainAnswerDto findQuestion(Integer id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("데이터를 찾을 수 없습니다."));
        List<AnswerDto> answerList = answerRepository.findAnswersByQuestionId(question.getId()).stream().map(AnswerDto::new).toList();
        return new QuestionContainAnswerDto(question, answerList);
    }

    public List<QuestionDto> findAll() {
        return questionRepository.findAll()
                .stream()
                .map(QuestionDto::new)
                .collect(Collectors.toList());
    }
}
