package org.sbb.sbb.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sbb.sbb.domain.mail.dto.MailDto.FindPw;
import org.sbb.sbb.domain.user.dto.UserDto;
import org.sbb.sbb.domain.user.dto.UserDto.UpdatePw;
import org.sbb.sbb.domain.user.dto.UserDto.UserJoin;
import org.sbb.sbb.domain.user.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.InputMismatchException;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/signup")
    public String signup(UserJoin userJoinDto) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserDto.UserJoin userJoin, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userJoin.getPassword().equals(userJoin.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.userJoin(userJoin);
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
    public String findPw(FindPw findPw) {
        return "password_find";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String userProfile(Model model, @RequestParam(value = "page", defaultValue = "0") int page, Authentication authentication) {
        UserDto.UserProfile profile = userService.getUserProfile(authentication.getName(), page);
        model.addAttribute("profile", profile);

        return "user_profile";
    }

    @GetMapping("/update/pw")
    @PreAuthorize("isAuthenticated()")
    public String updatePwPage(UpdatePw updatePw) {
        return "password_update";
    }

    @PostMapping("/update/pw")
    @PreAuthorize("isAuthenticated()")
    public String updatePw(@Valid UserDto.UpdatePw updatePw, BindingResult bindingResult, Authentication authentication) {
        try {
            userService.updatePassword(authentication.getName(), updatePw);
        } catch (InputMismatchException e) {
            bindingResult.reject("updateFailed", e.getMessage());
            return "redirect:/user/update/pw";
        }

        return "redirect:/user/profile";
    }

}
