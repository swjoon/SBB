package org.sbb.sbb.domain.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sbb.sbb.domain.mail.dto.MailDto;
import org.sbb.sbb.domain.mail.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.InputMismatchException;

@Controller
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/find/pw")
    public String findPw(@Valid MailDto.FindPw findPwDto, BindingResult bindingResult) {
        try {
            mailService.sendMail(findPwDto);
        } catch (InputMismatchException e) {
            bindingResult.reject("sendFailed", "email 이 일치하지 않습니다.");
            return "password_find";
        } catch (Exception e) {
            bindingResult.reject("sendFailed", e.getMessage());
            return "password_find";
        }

        return "redirect:/user/login";
    }

}
