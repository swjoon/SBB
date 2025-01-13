package org.sbb.sbb.board.board.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.board.answer.dto.req.AnswerSaveDto;
import org.sbb.sbb.board.answer.entity.Answer;
import org.sbb.sbb.board.answer.service.AnswerService;
import org.sbb.sbb.board.comment.domain.Comment;
import org.sbb.sbb.board.comment.domain.dto.req.CommentForm;
import org.sbb.sbb.board.comment.service.CommentService;
import org.sbb.sbb.board.question.domain.Question;
import org.sbb.sbb.board.question.domain.dto.resp.*;
import org.sbb.sbb.board.question.domain.dto.req.*;
import org.sbb.sbb.board.question.service.QuestionService;
import org.sbb.sbb.user.entity.User;
import org.sbb.sbb.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final UserService userService;
    private final AnswerService answerService;
    private final CommentService commentService;
    private final QuestionService questionService;

    public QuestionContainAnswerDto getQuestionDetail(int questionId) {
        Question question = questionService.getQuestion(questionId);

        return new QuestionContainAnswerDto(question);
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

    @Transactional
    public Comment createComment(CommentForm commentForm, String username, int id, String type) {
        User user = userService.getUser(username);

        Comment comment;

        if(type.equals("question")){
            Question question = questionService.getQuestion(id);
            comment = commentForm.toEntity(user, question, null);
        }else{
            Answer answer = answerService.getAnswer(id);
            comment = commentForm.toEntity(user, null, answer);
        }

        return commentService.createComment(comment);
    }
}
