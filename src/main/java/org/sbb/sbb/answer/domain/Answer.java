package org.sbb.sbb.answer.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sbb.sbb.question.domain.Question;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public String toString(){
        return "content : " + content +  " createDate : " + createDate;
    }
}
