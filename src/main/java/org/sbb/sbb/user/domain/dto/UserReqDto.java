package org.sbb.sbb.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.sbb.sbb.user.domain.Users;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;


public class UserReqDto {

    @Data
    @Builder
    public static class UserJoinReqDto {

        @NotEmpty(message = "사용자ID는 필수항목입니다.")
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

        public Users toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
            return Users.builder()
                    .username(this.username)
                    .password(bCryptPasswordEncoder.encode(this.password))
                    .nickname(this.nickname)
                    .email(this.email)
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build();
        }
    }


}
