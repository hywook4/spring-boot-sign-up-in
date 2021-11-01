package com.hywook4.signupin.controller.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordDto {
    private String name;
    private String phoneNumber;
    private String password;
}
