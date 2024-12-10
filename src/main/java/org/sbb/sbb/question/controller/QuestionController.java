package org.sbb.sbb.question.controller;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.question.domain.dto.QuestionRespDto.*;
import org.sbb.sbb.question.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String questionList(Model model) {
        List<QuestionDto> questionList = questionService.findAll();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String questionDetail(Model model, @PathVariable int id) {
        QuestionContainAnswerDto questionDto = questionService.findQuestion(id);
        model.addAttribute("questionDto", questionDto);
        return "question_detail";
    }

}
