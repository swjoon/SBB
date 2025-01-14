package org.sbb.sbb.domain.answer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sbb.sbb.domain.comment.entity.Comment;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
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

    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> voter;

    public String toString() {
        return "content : " + content + " createDate : " + createDate;
    }
}
