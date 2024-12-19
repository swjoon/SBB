package org.sbb.sbb.board.question.domain.dto.resp;

import lombok.Data;
import org.sbb.sbb.board.answer.domain.Answer;
import org.sbb.sbb.board.answer.domain.dto.AnswerRespDto;
import org.sbb.sbb.board.question.domain.Question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuestionContainAnswerDto {
    private Integer id;
    private String subject;
    private String content;
    private String username;
    private String nickname;
    private int voterSize;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private List<AnswerRespDto.GetAnswerDto> answerList;



    public QuestionContainAnswerDto(Question question, List<Answer> answerList) {
        this.id = question.getId();
        this.subject = question.getSubject();
        this.content = question.getContent();
        this.username = question.getUser().getUsername();
        this.nickname = question.getUser().getNickname();
        this.voterSize = question.getVoter().size();
        this.createDate = question.getCreateDate();
        this.modifyDate = question.getModifyDate();
        this.answerList = answerList.stream().map(AnswerRespDto.GetAnswerDto::new).collect(Collectors.toList());

    }

}