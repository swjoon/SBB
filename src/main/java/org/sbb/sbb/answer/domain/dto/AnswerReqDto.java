package org.sbb.sbb.answer.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.question.domain.Question;

import java.time.LocalDateTime;

public class AnswerReqDto {
    
    @Data
    public static class AnswerSaveDto {

        @NotEmpty(message = "내용은 필수항목입니다.")
        private String content;

        public Answer toEntity(Question question){
            return Answer.builder().content(this.content.trim()).question(question).createDate(LocalDateTime.now()).build();
        }

    }
}
