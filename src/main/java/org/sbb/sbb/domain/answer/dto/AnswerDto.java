package org.sbb.sbb.domain.answer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.comment.dto.CommentDto;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerDto {

    @Data
    public static class GetAnswer {
        private Integer id;
        private String content;
        private String username;
        private String nickname;
        private int voterSize;
        private LocalDateTime createDate;
        private LocalDateTime modifyDate;
        private List<CommentDto.GetComment> comments;

        public GetAnswer(Answer answer) {
            this.id = answer.getId();
            this.content = answer.getContent();
            this.username = answer.getUser().getUsername();
            this.nickname = answer.getUser().getNickname();
            this.voterSize = answer.getVoter().size();
            this.createDate = answer.getCreateDate();
            this.modifyDate = answer.getModifyDate();
            this.comments = answer.getCommentList().stream().map(CommentDto.GetComment::new).collect(Collectors.toList());
        }
    }

    @Data
    public static class AnswerSave {
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
