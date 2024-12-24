package org.sbb.sbb.board.answer.domain.dto.resp;

import lombok.Data;
import org.sbb.sbb.board.answer.domain.Answer;

import java.time.LocalDateTime;

@Data
public class GetAnswerDto {
    private Integer id;
    private String content;
    private String username;
    private String nickname;
    private int voterSize;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public GetAnswerDto(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.username = answer.getUser().getUsername();
        this.nickname = answer.getUser().getNickname();
        this.voterSize = answer.getVoter().size();
        this.createDate = answer.getCreateDate();
        this.modifyDate = answer.getModifyDate();
    }
}
