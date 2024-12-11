package org.sbb.sbb.question.domain.dto;

import lombok.Data;

import org.sbb.sbb.answer.domain.dto.AnswerRespDto.*;
import org.sbb.sbb.question.domain.Question;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionRespDto {

    @Data
    public static class GetQuestionDto {
        private Integer id;
        private String subject;
        private String content;
        private LocalDateTime createDate;

        public GetQuestionDto(Question question){
            this.id = question.getId();
            this.subject = question.getSubject();
            this.content = question.getContent();
            this.createDate = question.getCreateDate();
        }
    }

    @Data
    public static class QuestionContainAnswerDto {
        private Integer id;
        private String subject;
        private String content;
        private LocalDateTime createDate;
        private List<AnswerDto> answerList;

        public QuestionContainAnswerDto(Question question, List<AnswerDto> answerList){
            this.id = question.getId();
            this.subject = question.getSubject();
            this.content = question.getContent();
            this.createDate = question.getCreateDate();
            this.answerList = answerList;
        }

    }
}
