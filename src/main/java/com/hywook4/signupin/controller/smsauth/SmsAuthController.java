package com.hywook4.signupin.controller.smsauth;

import com.hywook4.signupin.controller.smsauth.dto.RequestSmsAuthDto;
import com.hywook4.signupin.controller.smsauth.dto.VerifySmsAuthDto;
import com.hywook4.signupin.controller.smsauth.dto.VerifySmsAuthResponseDto;
import com.hywook4.signupin.service.SmsService;
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
        String name = verifySmsAuthDto.getName();
        String phoneNumber = verifySmsAuthDto.getPhoneNumber();
        String verificationCode = verifySmsAuthDto.getVerificationCode();
        return new VerifySmsAuthResponseDto(name, phoneNumber, smsService.verifyCodeAndReturnToken(name, phoneNumber,verificationCode));
    }


}
