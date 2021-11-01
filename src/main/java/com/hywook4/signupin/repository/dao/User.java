package com.hywook4.signupin.repository.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String password;
    private String token;

    public User(String email, String nickname, String name, String phoneNumber, String password, String token){
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.token = token;
    }
}
