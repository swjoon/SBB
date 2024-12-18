package org.sbb.sbb.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sbb.sbb.answer.domain.Answer;
import org.sbb.sbb.answer.domain.dto.AnswerReqDto.*;
import org.sbb.sbb.answer.service.AnswerService;
import org.sbb.sbb.question.domain.Question;
import org.sbb.sbb.question.domain.dto.QuestionReqDto.*;
import org.sbb.sbb.question.domain.dto.QuestionRespDto.*;
import org.sbb.sbb.question.service.QuestionService;
import org.sbb.sbb.user.domain.User;
import org.sbb.sbb.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final UserService userService;
    private final AnswerService answerService;
    private final QuestionService questionService;

    @GetMapping("/list")
    public String questionList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<GetQuestionDto> paging = questionService.getQuestionList(page, kw);
        model.addAttribute("kw", kw);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String questionDetail(Model model, @PathVariable int id, AnswerSaveDto answerSaveDto) {
        List<Answer> answerList = answerService.findAll(id);
        QuestionContainAnswerDto questionDto = questionService.findQuestion(id , answerList);
        model.addAttribute("questionDto", questionDto);
        return "question_detail";
    }

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String questionForm(PostQuestionDto postQuestionDto) {
        return "question_form";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String questionCreate(@Valid PostQuestionDto postQuestionDto, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        User user = userService.getUser(authentication.getName());
        questionService.saveQuestion(postQuestionDto, user);
        return "redirect:/question/list";
    }

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(@PathVariable int id, PostQuestionDto postQuestionDto, Authentication authentication) {
        Question question = questionService.getQuestion(id);
        if(!question.getUser().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postQuestionDto.setSubject(question.getSubject());
        postQuestionDto.setContent(question.getContent());
        return "question_form";
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionModify(@PathVariable int id, @Valid PostQuestionDto postQuestionDto, BindingResult bindingResult, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        Question question = questionService.getQuestion(id);

        if(!question.getUser().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }

        questionService.modifyQuestion(postQuestionDto, question);

        return String.format("redirect:/question/detail/%s", id);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionDelete(@PathVariable int id, Authentication authentication) {
        Question question = questionService.getQuestion(id);

        if(!question.getUser().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
        }

        questionService.deleteQuestion(question);

        return "redirect:/question/list";
    }

    @PostMapping("/vote/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionVote(@PathVariable int id, Authentication authentication) {

        User user = userService.getUser(authentication.getName());
        Question question = questionService.getQuestion(id);

        questionService.voteQuestion(question, user);

        return String.format("redirect:/question/detail/%s", id);
    }


}
