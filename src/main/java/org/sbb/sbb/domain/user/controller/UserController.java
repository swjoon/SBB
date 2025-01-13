package org.sbb.sbb.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sbb.sbb.mail.dto.FindPwDto;
import org.sbb.sbb.mail.service.MailService;
import org.sbb.sbb.user.dto.req.UpdatePwDto;
import org.sbb.sbb.user.dto.req.UserJoinDto;
import org.sbb.sbb.user.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final MailService mailService;
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/signup")
    public String signup(UserJoinDto userJoinDto) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserJoinDto userJoinDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userJoinDto.getPassword().equals(userJoinDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.userJoin(userJoinDto);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/question/list";
    }

    @GetMapping("/find/pw")
    public String findPw(FindPwDto findPwDto) {
        return "password_find";
    }

    @PostMapping("/find/pw")
    public String findPw(@Valid FindPwDto findPwDto, BindingResult bindingResult) {
        try {
            userService.sendMail(findPwDto);

        } catch (NoSuchElementException e) {
            bindingResult.reject("정보 불일치", e.getMessage());
            return "password_find";
        }

        return "redirect:/user/find/pw";
    }

    //Todo: 유저 프로필 화면 구현
    @GetMapping("/profile")
    public String userProfile(Authentication authentication) {

        return "user_profile";
    }

    @GetMapping("/update/pw")
    public String updatePwPage(UpdatePwDto updatePwDto, BindingResult bindingResult) {

        //TODO: 유저 비밀번호 변경 페이지 구현 후 연결
        return "redirect:/user/find/pw";
    }

    @PostMapping("/update/pw")
    public String updatePw(@Valid UpdatePwDto updatePwDto, BindingResult bindingResult, Authentication authentication) {
        try {
            userService.updatePassword(authentication.getName(), updatePwDto);

        } catch (NoSuchElementException e) {
            bindingResult.reject("", e.getMessage());
            //Todo: 유저 비밀번호 변경 페이지 구현 후 연결
            return "redirect:/user/find/pw";
        }

        //TODO: 유저 정보 페이지 작성후 연결
        return "redirect:/user/find/pw";
    }

}
