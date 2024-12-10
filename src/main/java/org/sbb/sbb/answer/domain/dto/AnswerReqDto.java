package org.sbb.sbb.answer.domain.dto;

import lombok.Data;
import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.question.domain.Question;

import java.time.LocalDateTime;

public class AnswerReqDto {

    @Data
    public static class AnswerSaveDto {

        private String content;

        public Answer toEntity(Question question){
            return Answer.builder().content(this.content).question(question).createDate(LocalDateTime.now()).build();
        }

    }
}
