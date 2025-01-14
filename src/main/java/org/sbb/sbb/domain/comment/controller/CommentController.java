package org.sbb.sbb.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sbb.sbb.domain.board.service.BoardService;
import org.sbb.sbb.domain.comment.dto.CommentDto;
import org.sbb.sbb.domain.comment.service.CommentService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/{questionId}")
    public String questionCreateComment(Model model, @PathVariable Integer questionId, @Valid CommentDto.CommentForm commentForm,
                                        BindingResult bindingResult, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("questionDto", boardService.getQuestionDetail(questionId, 0, "id"));
            return "question_detail";
        }

        boardService.createComment(commentForm, authentication.getName(), questionId, "question");

        return String.format("redirect:/question/detail/%s", questionId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/{questionId}/answer/{answerId}")
    public String answerCreateComment(Model model, @PathVariable Integer questionId, @PathVariable Integer answerId,
                                      @Valid CommentDto.CommentForm commentForm, BindingResult bindingResult, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("questionDto", boardService.getQuestionDetail(questionId, 0, "id"));
        }

        boardService.createComment(commentForm, authentication.getName(), answerId, "answer");

        return String.format("redirect:/question/detail/%s", questionId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/{questionId}/delete/{commentId}")
    public String deleteComment(@PathVariable Integer questionId, @PathVariable Integer commentId,
                                Authentication authentication) {
        try {
            commentService.deleteComment(commentId, authentication.getName());
        } catch (AccessDeniedException e) {
            return String.format("redirect:/question/detail/%s", questionId);
        }

        return String.format("redirect:/question/detail/%s", questionId);
    }
}
