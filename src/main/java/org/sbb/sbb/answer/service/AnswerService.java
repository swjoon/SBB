package org.sbb.sbb.answer.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.answer.domain.dto.AnswerReqDto.*;
import org.sbb.sbb.answer.repository.AnswerRepository;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public void saveAnswer(int questionId, AnswerSaveDto answerSaveDto) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new NoSuchElementException("데이터를 찾을 수 없습니다."));
        answerRepository.save(answerSaveDto.toEntity(question));
    }

}
