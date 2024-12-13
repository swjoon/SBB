package org.sbb.sbb.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_USER("유저"), ROLE_ADMIN("관리자");

    private String value;
}
