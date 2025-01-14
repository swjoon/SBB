package org.sbb.sbb.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.common.redis.RedisRepository;
import org.sbb.sbb.domain.answer.dto.AnswerDto;
import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.answer.service.AnswerService;
import org.sbb.sbb.domain.comment.dto.CommentDto;
import org.sbb.sbb.domain.comment.entity.Comment;
import org.sbb.sbb.domain.comment.service.CommentService;
import org.sbb.sbb.domain.question.dto.QuestionDto;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.question.service.QuestionService;
import org.sbb.sbb.domain.user.entity.User;
import org.sbb.sbb.domain.user.service.UserService;
import org.springframework.data.domain.Page;
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
    private final RedisRepository redisRepository;

    public QuestionDto.QuestionContainAnswer getQuestionDetail(int questionId, int page, String type) {
        Question question = questionService.getQuestion(questionId);

        Page<AnswerDto.GetAnswer> answers = type.equals("descId") ?
                answerService.getAnswersById(question, page).map(AnswerDto.GetAnswer::new) :
                answerService.getAnswersByVoter(question, page).map(AnswerDto.GetAnswer::new);

        return new QuestionDto.QuestionContainAnswer(question, answers);
    }

    @Transactional
    public void saveQuestion(QuestionDto.PostQuestion postQuestion, String username) {
        User user = userService.getUser(username);
        questionService.saveQuestion(postQuestion, user);
    }

    @Transactional
    public void questionVote(int questionId, String username) {
        Question question = questionService.getQuestion(questionId);
        User user = userService.getUser(username);
        questionService.voteQuestion(question, user);
    }

    @Transactional
    public Answer saveAnswer(int questionId, String username, AnswerDto.AnswerSave answerSave) {
        User user = userService.getUser(username);
        Question question = questionService.getQuestion(questionId);
        return answerService.saveAnswer(answerSave, user, question);
    }

    @Transactional
    public Answer answerVote(int answerId, String username) {
        User user = userService.getUser(username);
        Answer answer = answerService.getAnswer(answerId);
        return answerService.voteAnswer(answer, user);
    }

    @Transactional
    public void createComment(CommentDto.CommentForm commentForm, String username, int id, String type) {
        User user = userService.getUser(username);

        Comment comment = type.equals("question")?
                commentForm.toEntity(user,questionService.getQuestion(id),null):
                commentForm.toEntity(user,null,answerService.getAnswer(id));

        commentService.createComment(comment);
    }

    @Transactional
    public void increaseViewCount(int questionId, String username) {
        if(redisRepository.isVisited(username, questionId)){
            return;
        }

        redisRepository.save(username, questionId);

        questionService.incrementViewCount(questionId);
    }
}
