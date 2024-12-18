package org.sbb.sbb.board.question.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sbb.sbb.board.answer.domain.Answer;
import org.sbb.sbb.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table(name = "question")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @JoinColumn(name = "users_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answerList;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> voter;

    public String toString(){
        return "subject : " + subject + " content : " + content;
    }
}
