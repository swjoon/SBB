package org.sbb.sbb.question.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.user.domain.Users;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "question")
@Entity
@Getter
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answerList;

    public String toString(){
        return "subject : " + subject + " content : " + content;
    }
}
