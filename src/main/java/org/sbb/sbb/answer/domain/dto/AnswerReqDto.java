package org.sbb.sbb.answer.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.user.domain.User;

import java.time.LocalDateTime;

public class AnswerReqDto {
    
    @Data
    public static class AnswerSaveDto {

        @NotEmpty(message = "내용은 필수항목입니다.")
        private String content;

        public Answer toEntity(User user, Question question){
            return Answer.builder()
                    .content(this.content.trim())
                    .user(user)
                    .question(question)
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build();
        }

    }
}
