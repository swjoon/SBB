package org.sbb.sbb.domain.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.comment.entity.Comment;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.user.entity.User;

import java.time.LocalDateTime;

public class CommentDto {

    @Data
    @Builder
    public static class CommentForm {

        @NotEmpty(message = "내용을 입력해주세요.")
        private String content;

        public Comment toEntity(User user, Question question, Answer answer) {
            return Comment.builder()
                    .content(this.content)
                    .user(user)
                    .question(question)
                    .answer(answer)
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    public static class GetComment {

        private Integer id;
        private String content;
        private String username;
        private String nickname;
        private LocalDateTime createDate;
        private LocalDateTime modifyDate;
        private boolean type;

        public GetComment(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.username = comment.getUser().getUsername();
            this.nickname = comment.getUser().getNickname();
            this.createDate = comment.getCreateDate();
            this.modifyDate = comment.getModifyDate();
            this.type = (comment.getAnswer() == null);
        }
    }
}
