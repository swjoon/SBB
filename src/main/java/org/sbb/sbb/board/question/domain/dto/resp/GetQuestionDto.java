package org.sbb.sbb.board.question.domain.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetQuestionDto {
    private Integer id;
    private String subject;
    private String content;
    private String username;
    private String nickname;
    private Long answerCount;
    private LocalDateTime modifyDate;
}
