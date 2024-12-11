package org.sbb.sbb.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sbb.sbb.answer.domain.dto.AnswerReqDto.*;
import org.sbb.sbb.answer.service.AnswerService;
import org.sbb.sbb.question.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @PostMapping("/{id}")
    public String submit(Model model, @PathVariable int id, @Valid AnswerSaveDto answerSaveDto , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("questionDto", questionService.findQuestion(id));
            return "question_detail";
        }
        answerService.saveAnswer(id, answerSaveDto);
        return "redirect:/question/detail/" + id;
    }

}
