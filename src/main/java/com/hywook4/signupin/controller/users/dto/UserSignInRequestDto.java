package com.hywook4.signupin.controller.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignInRequestDto {
    private String idField;
    private String idValue;
    private String password;
}
