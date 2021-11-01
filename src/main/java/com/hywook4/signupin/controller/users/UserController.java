package com.hywook4.signupin.controller.users;


import com.hywook4.signupin.controller.users.dto.*;
import com.hywook4.signupin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/my-info", method = RequestMethod.GET)
    public UserInfoDto getUserInfo(@RequestHeader("Authorization") String bearerToken) {
        return userService.getUserInfo(bearerToken);
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public UserInfoDto signUp(@RequestHeader("X-Sms-VerifiedToken") String smsVerifiedToken, @RequestBody UserSignUpDto userSignUpDto) {
        return userService.signUpUser(smsVerifiedToken, userSignUpDto);
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public UserSignInResponseDto signIn(@RequestBody UserSignInRequestDto userSignInRequestDto) {
        return userService.signInUser(userSignInRequestDto.getIdField(), userSignInRequestDto.getIdValue(), userSignInRequestDto.getPassword());
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public void changePassword(@RequestHeader("X-Sms-VerifiedToken") String smsVerifiedToken, @RequestBody UserChangePasswordDto userChangePasswordDto) {
        userService.changePassword(smsVerifiedToken, userChangePasswordDto);
    }
}
