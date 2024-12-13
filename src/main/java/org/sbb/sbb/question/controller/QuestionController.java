package org.sbb.sbb.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sbb.sbb.answer.domain.dto.AnswerReqDto.*;
import org.sbb.sbb.question.domain.dto.QuestionReqDto.*;
import org.sbb.sbb.question.domain.dto.QuestionRespDto.*;
import org.sbb.sbb.question.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String questionList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<GetQuestionDto> paging = questionService.getQuestionList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String questionDetail(Model model, @PathVariable int id, AnswerSaveDto answerSaveDto) {
        QuestionContainAnswerDto questionDto = questionService.findQuestion(id);
        model.addAttribute("questionDto", questionDto);
        return "question_detail";
    }

    @GetMapping("/create")
    public String questionForm(PostQuestionDto postQuestionDto) {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid PostQuestionDto postQuestionDto, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        questionService.saveQuestion(authentication.getName(),postQuestionDto);
        return "redirect:/question/list";
    }

}
