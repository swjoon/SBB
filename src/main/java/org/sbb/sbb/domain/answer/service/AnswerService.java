package org.sbb.sbb.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.common.exception.DataNotFoundException;
import org.sbb.sbb.domain.answer.dto.AnswerDto;
import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.answer.repository.AnswerRepository;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer getAnswer(int id) {
        return answerRepository.findById(id).orElseThrow(() -> new DataNotFoundException("데이터를 찾을 수 없습니다."));
    }

    public List<Answer> getAnswerList(Question question) {
        return answerRepository.findAnswersByQuestion(question);
    }

    // type = { id (default), voter }
    public Page<Answer> getAnswersById(Question question, int page) {
        List<Sort.Order> list = new ArrayList<>();
        list.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(list));
        return answerRepository.findAnswersByQuestion(question, pageable);
    }

    public Page<Answer> getAnswersByVoter(Question question, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return answerRepository.findAnswersByVoter(question, pageable);
    }

    @Transactional
    public Answer saveAnswer(AnswerDto.AnswerSave answerSave, User user, Question question) {
        return answerRepository.save(answerSave.toEntity(user, question));
    }

    @Transactional
    public void modifyAnswer(AnswerDto.AnswerSave answerSave, Answer answer) {
        answer.setContent(answerSave.getContent().trim());
        answer.setModifyDate(LocalDateTime.now());
        answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(Answer answer) {
        answerRepository.delete(answer);
    }

    @Transactional
    public Answer voteAnswer(Answer answer, User user) {
        if (answer.getVoter().contains(user)) {
            answer.getVoter().remove(user);
        } else {
            answer.getVoter().add(user);
        }
        return answerRepository.save(answer);
    }

    public Page<Answer> getAnswerPage(int page) {
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
        return answerRepository.findAll(pageable);
    }
}
