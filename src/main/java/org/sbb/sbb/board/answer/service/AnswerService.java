package org.sbb.sbb.board.answer.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.board.answer.domain.Answer;
import org.sbb.sbb.board.answer.domain.dto.AnswerReqDto.*;
import org.sbb.sbb.board.answer.repository.AnswerRepository;
import org.sbb.sbb.board.question.domain.Question;
import org.sbb.sbb.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer getAnswer(int id){
        return answerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("데이터를 찾을 수 없습니다."));
    }

    public List<Answer> findAll(int questionId){
        return answerRepository.findAnswersByQuestionId(questionId);
    }

    @Transactional
    public Answer saveAnswer(AnswerSaveDto answerSaveDto, User user, Question question) {
        Answer answer = answerRepository.save(answerSaveDto.toEntity(user, question));
        return answer;
    }

    @Transactional
    public void modifyAnswer(AnswerSaveDto answerSaveDto, Answer answer) {
        answer.setContent(answerSaveDto.getContent().trim());
        answer.setModifyDate(LocalDateTime.now());
        answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(Answer answer) {
        answerRepository.delete(answer);
    }

    @Transactional
    public Answer voteAnswer(Answer answer, User user) {
        answer.getVoter().add(user);
        return answerRepository.save(answer);
    }
}
