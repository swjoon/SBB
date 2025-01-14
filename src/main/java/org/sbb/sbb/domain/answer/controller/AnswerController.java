package org.sbb.sbb.domain.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sbb.sbb.domain.answer.dto.AnswerDto;
import org.sbb.sbb.domain.answer.entity.Answer;
import org.sbb.sbb.domain.answer.service.AnswerService;
import org.sbb.sbb.domain.board.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final BoardService boardService;
    private final AnswerService answerService;

    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String submit(Model model, @PathVariable int id, @Valid AnswerDto.AnswerSave answerSave, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("questionDto", boardService.getQuestionDetail(id, 0, "id"));
            return "question_detail";
        }
        Answer answer = boardService.saveAnswer(id, authentication.getName(), answerSave);
        return String.format("redirect:/question/detail/%s#answer_%s", id, answer.getId());
    }

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String answerModify(@PathVariable Integer id, AnswerDto.AnswerSave answerSave, Authentication authentication) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getUser().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerSave.setContent(answer.getContent());

        return "answer_form";
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String answerModify(@PathVariable Integer id, @Valid AnswerDto.AnswerSave answerSave, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }

        Answer answer = answerService.getAnswer(id);

        if (!answer.getUser().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerService.modifyAnswer(answerSave, answer);

        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String answerDelete(@PathVariable Integer id, Authentication authentication) {
        Answer answer = this.answerService.getAnswer(id);

        if (!answer.getUser().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        answerService.deleteAnswer(answer);

        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    @PostMapping("/vote/{id}")
    @PreAuthorize("isAuthenticated()")
    public String answerVote(@PathVariable Integer id, Authentication authentication) {

        Answer answer = boardService.answerVote(id, authentication.getName());

        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }
}