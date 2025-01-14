package org.sbb.sbb.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.common.exception.DataNotFoundException;
import org.sbb.sbb.domain.comment.entity.Comment;
import org.sbb.sbb.domain.comment.repository.CommentRepository;
import org.sbb.sbb.domain.question.dto.QuestionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment getComment(int id) {
        return commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("일치하는 데이터가 없습니다."));
    }

    @Transactional
    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(int commentId, String username) {
        Comment comment = getComment(commentId);

        if(!comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        commentRepository.deleteById(commentId);
    }

    public Page<Comment> getComments(int page) {
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
        return commentRepository.findAll(pageable);
    }
}
