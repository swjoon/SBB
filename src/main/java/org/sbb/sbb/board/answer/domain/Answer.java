package org.sbb.sbb.board.answer.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sbb.sbb.board.question.domain.Question;
import org.sbb.sbb.user.domain.User;

import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "answer")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @JoinColumn(name = "users_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(name = "question_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> voter;

    public String toString(){
        return "content : " + content +  " createDate : " + createDate;
    }
}
