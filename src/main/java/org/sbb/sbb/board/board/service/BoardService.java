package org.sbb.sbb.board.board.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.board.answer.domain.Answer;
import org.sbb.sbb.board.answer.domain.dto.AnswerReqDto.*;
import org.sbb.sbb.board.answer.service.AnswerService;
import org.sbb.sbb.board.question.domain.Question;
import org.sbb.sbb.board.question.domain.dto.QuestionReqDto.*;
import org.sbb.sbb.board.question.domain.dto.QuestionRespDto.*;
import org.sbb.sbb.board.question.service.QuestionService;
import org.sbb.sbb.user.domain.User;
import org.sbb.sbb.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final UserService userService;
    private final AnswerService answerService;
    private final QuestionService questionService;

    public QuestionContainAnswerDto getQustionDetail(int questionId) {
        List<Answer> answerList = answerService.findAll(questionId);
        return questionService.findQuestion(questionId, answerList);
    }

    @Transactional
    public void saveQuestion(PostQuestionDto postQuestionDto, String username) {
        User user = userService.getUser(username);
        questionService.saveQuestion(postQuestionDto, user);
    }

    @Transactional
    public void questionVote(int questionId, String username) {
        Question question = questionService.getQuestion(questionId);
        User user = userService.getUser(username);
        questionService.voteQuestion(question, user);
    }

    public QuestionContainAnswerDto findQuestion(int questionId) {
        List<Answer> answerList = answerService.findAll(questionId);
        return questionService.findQuestion(questionId, answerList);
    }

    @Transactional
    public Answer saveAnswer(int questionId, String username, AnswerSaveDto answerSaveDto) {
        User user = userService.getUser(username);
        Question question = questionService.getQuestion(questionId);
        return answerService.saveAnswer(answerSaveDto, user, question);
    }

    @Transactional
    public Answer answerVote(int answerId, String username) {
        User user = userService.getUser(username);
        Answer answer = answerService.getAnswer(answerId);
        return answerService.voteAnswer(answer, user);
    }


}
