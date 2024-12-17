package org.sbb.sbb.question.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.user.domain.User;

import java.time.LocalDateTime;

public class QuestionReqDto {

    @Data
    public static class PostQuestionDto {
        @NotEmpty(message = "제목은 필수항목입니다.")
        @Size(max = 200)
        private String subject;

        @NotEmpty(message = "내용은 필수항목입니다.")
        private String content;

        public Question toEntity(User user){
            return Question.builder()
                    .subject(this.subject.trim())
                    .content(this.content.trim())
                    .user(user)
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build();
        }
    }
}
