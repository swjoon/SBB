package org.sbb.sbb.domain.question.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sbb.sbb.common.util.CommonUtil;
import org.sbb.sbb.domain.answer.dto.AnswerDto;
import org.sbb.sbb.domain.board.service.BoardService;
import org.sbb.sbb.domain.comment.dto.CommentDto;
import org.sbb.sbb.domain.question.dto.QuestionDto;
import org.sbb.sbb.domain.question.entity.Question;
import org.sbb.sbb.domain.question.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final BoardService boardService;
    private final QuestionService questionService;
    private final CommonUtil commonUtil;

    @GetMapping("/list")
    public String questionList(Model model,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<QuestionDto.GetQuestion> paging = questionService.getQuestionPage(page, kw);
        model.addAttribute("kw", kw);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String questionDetail(Model model,
                                 @PathVariable int id,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "type", defaultValue = "descId") String type,
                                 AnswerDto.AnswerSave answerSave,
                                 CommentDto.CommentForm commentForm,
                                 Authentication authentication,
                                 HttpServletRequest request) {

        boardService.increaseViewCount(id, authentication != null ? authentication.getName() : commonUtil.generateAnonymousKey(request));

        QuestionDto.QuestionContainAnswer questionDto = boardService.getQuestionDetail(id, page, type);
        model.addAttribute("questionDto", questionDto);

        return "question_detail";
    }

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String questionForm(QuestionDto.PostQuestion postQuestion) {
        return "question_form";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String questionCreate(@Valid QuestionDto.PostQuestion postQuestion, BindingResult bindingResult, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        boardService.saveQuestion(postQuestion, authentication.getName());
        return "redirect:/question/list";
    }

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(@PathVariable int id, QuestionDto.PostQuestion postQuestion, Authentication authentication) {
        Question question = questionService.getQuestion(id);
        if (!question.getUser().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postQuestion.setSubject(question.getSubject());
        postQuestion.setContent(question.getContent());
        return "question_form";
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(@PathVariable int id, @Valid QuestionDto.PostQuestion postQuestion, BindingResult bindingResult, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        Question question = questionService.getQuestion(id);

        if (!question.getUser().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        questionService.modifyQuestion(postQuestion, question);

        return String.format("redirect:/question/detail/%s", id);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionDelete(@PathVariable int id, Authentication authentication) {
        Question question = questionService.getQuestion(id);

        if (!question.getUser().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        questionService.deleteQuestion(question);

        return "redirect:/question/list";
    }

    @PostMapping("/vote/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionVote(@PathVariable int id, Authentication authentication) {

        boardService.questionVote(id, authentication.getName());

        return String.format("redirect:/question/detail/%s", id);
    }
}
