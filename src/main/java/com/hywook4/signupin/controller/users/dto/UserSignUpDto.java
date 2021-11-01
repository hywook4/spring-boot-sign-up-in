package com.hywook4.signupin.controller.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpDto {
    private String email;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String password;
}
