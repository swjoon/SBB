package org.sbb.sbb.domain.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

public class MailDto {

    @Data
    public static class FindPw {

        @NotEmpty(message = "Id는 필수항목입니다.")
        private String username;

        @Email
        @NotEmpty(message = "Email은 필수항목입니다.")
        private String email;
    }
}
