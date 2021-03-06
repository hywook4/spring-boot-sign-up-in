package com.hywook4.signupin.controller.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String email;
    private String nickname;
    private String name;
    private String phoneNumber;
}
