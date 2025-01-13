package org.sbb.sbb.domain.question.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.sbb.sbb.domain.answer.dto.AnswerDto;
import org.sbb.sbb.domain.comment.dto.CommentDto;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDto {

    @Data
    public static class GetQuestion {
        private Integer id;
        private String subject;
        private String content;
        private String username;
        private String nickname;
        private int answerCount;
        private int viewCount;
        private LocalDateTime modifyDate;

        public GetQuestion(Question question) {
            this.id = question.getId();
            this.subject = question.getSubject();
            this.content = question.getContent();
            this.username = question.getUser().getUsername();
            this.nickname = question.getUser().getNickname();
            this.answerCount = question.getAnswerList().size();
            this.viewCount = question.getViewCount();
            this.modifyDate = question.getModifyDate();
        }
    }

    @Data
    public static class PostQuestion {
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
                    .viewCount(0)
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    public static class QuestionContainAnswer {
        private Integer id;
        private String subject;
        private String content;
        private String username;
        private String nickname;
        private int voterSize;
        private int viewCount;
        private LocalDateTime modifyDate;
        private Page<AnswerDto.GetAnswer> answers;
        private List<CommentDto.GetComment> comments;


        public QuestionContainAnswer(Question question, Page<AnswerDto.GetAnswer> answers) {
            this.id = question.getId();
            this.subject = question.getSubject();
            this.content = question.getContent();
            this.username = question.getUser().getUsername();
            this.nickname = question.getUser().getNickname();
            this.voterSize = question.getVoter().size();
            this.viewCount = question.getViewCount();
            this.modifyDate = question.getModifyDate();
            this.answers = answers;
            this.comments = question.getCommentList().stream().map(CommentDto.GetComment::new).collect(Collectors.toList());
        }
    }
}
