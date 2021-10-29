package com.hywook4.signupin.controller.smsauth;

import com.hywook4.signupin.controller.smsauth.dto.RequestSmsAuthDto;
import com.hywook4.signupin.controller.smsauth.dto.VerifySmsAuthDto;
import com.hywook4.signupin.controller.smsauth.dto.VerifySmsAuthResponseDto;
import com.hywook4.signupin.service.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms-auth")
public class SmsAuthController {

    private final SmsService smsService;

    @Autowired
    public SmsAuthController(SmsService smsService){
        this.smsService = smsService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void requestSmsAuth(@RequestBody RequestSmsAuthDto requestSmsAuthDto) {
        smsService.storeSmsVerificationCode(requestSmsAuthDto.getName(), requestSmsAuthDto.getPhoneNumber());
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public VerifySmsAuthResponseDto verifySmsAuth(@RequestBody VerifySmsAuthDto verifySmsAuthDto) {
        //TODO
        // 이름, 전화번호로 만들어진 키로 Redis에 접근하여 값과 비교하여서 인증 성공, 혹은 실패 여부를 알림.
        // 식별하기 위한 토큰을 리턴해줌. (key와 함께 리턴해줌)
        String name = verifySmsAuthDto.getName();
        String phoneNumber = verifySmsAuthDto.getPhoneNumber();
        String verificationCode = verifySmsAuthDto.getVerificationCode();
        return new VerifySmsAuthResponseDto(name, phoneNumber, smsService.verifyCodeAndReturnToken(name, phoneNumber,verificationCode));
    }


}
