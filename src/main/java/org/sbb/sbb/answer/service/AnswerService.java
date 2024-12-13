package org.sbb.sbb.answer.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.answer.domain.dto.AnswerReqDto.*;
import org.sbb.sbb.answer.domain.dto.AnswerRespDto.*;
import org.sbb.sbb.answer.repository.AnswerRepository;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.question.service.QuestionService;
import org.sbb.sbb.user.domain.Users;
import org.sbb.sbb.user.service.UserService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final UserService userService;
    private final QuestionService questionService;
    private final AnswerRepository answerRepository;

    @Transactional
    public void saveAnswer(int questionId, String username, AnswerSaveDto answerSaveDto) {
        Question question = questionService.getQuestion(questionId);
        Users user = userService.getUser(username);
        answerRepository.save(answerSaveDto.toEntity(user, question));
    }

    public List<Answer> findAll(int questionId){
        return answerRepository.findAnswersByQuestionId(questionId);
    }

}
