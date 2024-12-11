package org.sbb.sbb.question.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.answer.domain.dto.AnswerRespDto.*;
import org.sbb.sbb.answer.repository.AnswerRepository;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.question.domain.dto.QuestionReqDto.*;
import org.sbb.sbb.question.domain.dto.QuestionRespDto.*;
import org.sbb.sbb.question.repository.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionContainAnswerDto findQuestion(Integer id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("데이터를 찾을 수 없습니다."));
        List<AnswerDto> answerList = answerRepository.findAnswersByQuestionId(question.getId()).stream().map(AnswerDto::new).toList();
        return new QuestionContainAnswerDto(question, answerList);
    }

    public List<GetQuestionDto> findAll() {
        return questionRepository.findAll()
                .stream()
                .map(GetQuestionDto::new)
                .collect(Collectors.toList());
    }

    public void saveQuestion(PostQuestionDto postQuestionDto) {
        questionRepository.save(postQuestionDto.toEntity());
    }

    public Page<GetQuestionDto> getQuestionList(int page){
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sort));
        return questionRepository.findAll(pageable).map(GetQuestionDto::new);
    }
}
