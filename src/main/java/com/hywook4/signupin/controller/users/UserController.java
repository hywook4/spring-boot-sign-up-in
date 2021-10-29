package com.hywook4.signupin.controller.users;


import com.hywook4.signupin.controller.users.dto.UserInfoDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @RequestMapping(value="/info", method = RequestMethod.GET)
    public UserInfoDto getUserInfo() {
        //TODO
        // 1. 토큰 확인
        // 2. 토큰이 유요한 경우 특정 유저의 정보 가져와서 보여주기.
        return new UserInfoDto();
    }

    @RequestMapping(value="/sign-up", method = RequestMethod.POST)
    public void signUp() {
        //TODO
        // 1. 문자 인증을 통해 받은 token과 함께 이메일, 닉네임, 비밀번호(공개키로 암호화된), 이름, 전화번호를 받음
        // 2. token의 유효성을 redis에서 받아와서 확인
        // 3. 토큰이 유효할 경우 데이터 유효성 검사
        // 4. 데이터 넣기 + 개인 토큰 / 시크릿키 생
        // 4. 성공 혹은 실패 여부 리턴
    }

    @RequestMapping(value="/sign-in", method = RequestMethod.POST)
    public void signIn() {
        //TODO
        // 1. 이메일 혹은 전화번호 + 비밀번호(공개키로 암호화된)를 받아옴
        // 2. 비밀번호가 일치하는지 확인
        // 3. 일치 시 로그인 토큰 제
    }

    // TO-DO: put인지 post인지 확인하고 고치기
    @RequestMapping(value="/change-password", method = RequestMethod.POST)
    public void changePassword() {
        //TODO
        // 1. 문자 인증을 통해 받은 token과 함께 새로운 비밀번호 (공개키로 암호화된) 받아오기
        // 2. 비밀번호 유효성 검사, 및 업데이트
        // 3. 성공여부 리
    }
}
