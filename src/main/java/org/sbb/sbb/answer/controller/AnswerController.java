package org.sbb.sbb.answer.controller;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.answer.domain.dto.AnswerReqDto.*;
import org.sbb.sbb.answer.service.AnswerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/{id}")
    public String submit(@PathVariable int id, @ModelAttribute AnswerSaveDto answerSaveDto) {
        answerService.saveAnswer(id, answerSaveDto);
        return "redirect:/question/detail/" + id;
    }
}
