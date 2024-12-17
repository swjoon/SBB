package org.sbb.sbb.question.domain.dto;

import lombok.Data;

import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.answer.domain.dto.AnswerRespDto.*;
import org.sbb.sbb.question.domain.Question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionRespDto {

    @Data
    public static class GetQuestionDto {
        private Integer id;
        private String subject;
        private String content;
        private String username;
        private String nickname;
        private LocalDateTime createDate;
        private LocalDateTime modifyDate;
        private List<GetAnswerDto> answerList;

        public GetQuestionDto(Question question) {
            this.id = question.getId();
            this.subject = question.getSubject();
            this.content = question.getContent();
            this.username = question.getUser().getUsername();
            this.nickname = question.getUser().getNickname();
            this.createDate = question.getCreateDate();
            this.modifyDate = question.getModifyDate();
            this.answerList = question.getAnswerList().stream().map(GetAnswerDto::new).collect(Collectors.toList());
        }
    }

    @Data
    public static class QuestionContainAnswerDto {
        private Integer id;
        private String subject;
        private String content;
        private String username;
        private String nickname;
        private int voterSize;
        private LocalDateTime createDate;
        private LocalDateTime modifyDate;
        private List<GetAnswerDto> answerList;



        public QuestionContainAnswerDto(Question question, List<Answer> answerList) {
            this.id = question.getId();
            this.subject = question.getSubject();
            this.content = question.getContent();
            this.username = question.getUser().getUsername();
            this.nickname = question.getUser().getNickname();
            this.voterSize = question.getVoter().size();
            this.createDate = question.getCreateDate();
            this.modifyDate = question.getModifyDate();
            this.answerList = answerList.stream().map(GetAnswerDto::new).collect(Collectors.toList());

        }

    }
}
