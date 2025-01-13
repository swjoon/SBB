package org.sbb.sbb.board.answer.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.board.answer.dto.req.AnswerSaveDto;
import org.sbb.sbb.board.answer.entity.Answer;
import org.sbb.sbb.board.answer.repository.AnswerRepository;
import org.sbb.sbb.board.question.domain.Question;
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
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer getAnswer(int id) {
        return answerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("데이터를 찾을 수 없습니다."));
    }

    public List<Answer> getAnswerList(int questionId) {
        return answerRepository.findAnswersByQuestionId(questionId);
    }

    // type = { id (default), voter }
    public Page<Answer> getAnswerPage(int questionId, int page, String type) {
        List<Sort.Order> list = new ArrayList<>();
        list.add(Sort.Order.asc(type));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(list));
        return answerRepository.findAnswersByQuestionId(questionId, pageable);
    }

    @Transactional
    public Answer saveAnswer(AnswerSaveDto answerSaveDto, User user, Question question) {
        return answerRepository.save(answerSaveDto.toEntity(user, question));
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
