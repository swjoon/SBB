package org.sbb.sbb.domain.question.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.category.domain.Category;
import org.sbb.sbb.domain.comment.domain.Comment;
import org.sbb.sbb.domain.user.entity.User;

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

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> voter;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    public String toString(){
        return "subject : " + subject + " content : " + content;
    }
}
