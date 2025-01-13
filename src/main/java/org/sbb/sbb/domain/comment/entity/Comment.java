package org.sbb.sbb.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
