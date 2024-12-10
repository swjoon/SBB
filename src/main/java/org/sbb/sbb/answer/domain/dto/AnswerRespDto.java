package org.sbb.sbb.answer.domain.dto;

import lombok.Data;
import org.sbb.sbb.answer.domain.Answer;

import java.time.LocalDateTime;

public class AnswerRespDto {

    @Data
    public static class AnswerDto {
        private Integer id;
        private String content;
        private LocalDateTime createDate;

        public AnswerDto(Answer answer) {
            this.id = answer.getId();
            this.content = answer.getContent();
            this.createDate = answer.getCreateDate();
        }
    }
}
