package org.sbb.sbb.dummy;

import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.question.domain.Question;

import java.time.LocalDateTime;

public class DummyObject {

    protected static Question newQuestion(String subject, String content) {

        return Question.builder().subject(subject).content(content).createDate(LocalDateTime.now()).build();
    }

    protected  static Answer newAnswer(String content, Question question) {
        return Answer.builder().content(content).question(question).createDate(LocalDateTime.now()).build();
    }
}
