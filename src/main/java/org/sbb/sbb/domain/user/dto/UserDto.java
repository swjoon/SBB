package org.sbb.sbb.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.sbb.sbb.domain.question.dto.QuestionDto;
import org.sbb.sbb.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class UserDto {

    @Data
    public static class UpdatePw {
        @NotEmpty(message = "기존 비밀번호를 입력해주세요")
        private String oldPassword;

        @NotEmpty(message = "변경할 비밀번호를 입력해주세요")
        private String newPassword;
    }

    @Data
    @Builder
    public static class UserJoin {
        @NotEmpty(message = "사용자 ID는 필수항목입니다.")
        String username;

        @NotEmpty(message = "비밀번호는 필수항목입니다.")
        String password;

        @NotEmpty(message = "2차 확인 비밀번호는 필수항목입니다.")
        String passwordConfirm;

        @NotEmpty(message = "닉네임은 필수항목입니다.")
        String nickname;

        @Email
        @NotEmpty(message = "이메일은 필수항목입니다.")
        String email;

        public User toEntity(String encodedPassword) {
            return User.builder()
                    .username(this.username)
                    .password(encodedPassword)
                    .nickname(this.nickname)
                    .email(this.email)
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    public static class UserProfile {
        private String username;
        private String nickname;
        private String email;
        private String provider;
        private LocalDateTime createDate;
        private Page<QuestionDto.GetQuestion> questions;

        public UserProfile(User user , Page<QuestionDto.GetQuestion> questions) {
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.email = user.getEmail();
            this.provider = user.getProvider();
            this.createDate = user.getCreateDate();
            this.questions = questions;
        }
    }
}
