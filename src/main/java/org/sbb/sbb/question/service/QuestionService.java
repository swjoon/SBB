package org.sbb.sbb.question.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.answer.domain.dto.AnswerRespDto.*;
import org.sbb.sbb.answer.repository.AnswerRepository;
import org.sbb.sbb.answer.service.AnswerService;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.question.domain.dto.QuestionReqDto.*;
import org.sbb.sbb.question.domain.dto.QuestionRespDto.*;
import org.sbb.sbb.question.repository.QuestionRepository;
import org.sbb.sbb.user.domain.Users;
import org.sbb.sbb.user.service.UserService;
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

    private final UserService userService;
    private final AnswerService answerService;
    private final QuestionRepository questionRepository;

    public Question getQuestion(int id){
        return questionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("데이터를 찾을 수 없습니다."));
    }


    public QuestionContainAnswerDto findQuestion(int id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("데이터를 찾을 수 없습니다."));
        List<Answer> answerList = answerService.findAll(question.getId());
        return new QuestionContainAnswerDto(question, answerList);
    }

    public List<GetQuestionDto> findAll() {
        return questionRepository.findAll()
                .stream()
                .map(GetQuestionDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveQuestion(String username,PostQuestionDto postQuestionDto) {
        Users user = userService.getUser(username);
        questionRepository.save(postQuestionDto.toEntity(user));
    }

    public Page<GetQuestionDto> getQuestionList(int page){
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sort));
        return questionRepository.findAll(pageable).map(GetQuestionDto::new);
    }
}
