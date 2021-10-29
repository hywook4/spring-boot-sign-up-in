package com.hywook4.signupin.controller.smsauth;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hywook4.signupin.controller.smsauth.dto.RequestSmsAuthDto;
import com.hywook4.signupin.controller.smsauth.dto.VerifySmsAuthDto;

@RestController
@RequestMapping("/sms-auth")
public class SmsAuthController {


    @RequestMapping(value = "", method = RequestMethod.POST)
    public String requestSmsAuth(@RequestBody RequestSmsAuthDto requestSmsAuthDto) {
        //TODO
        // 이름, 전화번호를 받아서 SMS 서버로 인증 번호 요청.
        // 이후 인증 번호 redis에 약 5분 expired로 넣어두기 (key는 이름 전화번호?)

        return "/sms-auth";
    }

    @RequestMapping(value="/verify", method = RequestMethod.POST)
    public String verifySmsAuth(@RequestBody VerifySmsAuthDto verifySmsAuthDto) {
        //TODO
        // 이름, 전화번호로 만들어진 키로 Redis에 접근하여 값과 비교하여서 인증 성공, 혹은 실패 여부를 알림.
        // 식별하기 위한 토큰을 리턴해줌. (key와 함께 리턴해줌)
        return "/sms-auth/verify";
    }


}
