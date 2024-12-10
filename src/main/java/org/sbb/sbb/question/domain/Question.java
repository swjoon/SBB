package org.sbb.sbb.question.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sbb.sbb.answer.domain.Answer;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    public String toString(){
        return "subject : " + subject + " content : " + content;
    }
}
