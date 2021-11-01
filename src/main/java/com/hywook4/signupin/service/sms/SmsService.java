package com.hywook4.signupin.service.sms;

import com.hywook4.signupin.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class SmsService {

    @Value("${sms.code.timeout}")
    private int codeTtl;

    @Value("${sms.token.timeout}")
    private int tokenTtl;

    private final RedisRepository redisRepository;

    @Autowired
    public SmsService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public void storeSmsVerificationCode(String name, String phoneNumber) {
        // String verificationCode =  sendSmsVerificationCode(name, phoneNumber);
        // since no SMS service is required, create random verification code.
        String verificationCode = makeVerificationCode();

        // if name, phoneNumber is not valid, throw exception and return error
        // else store the code to redis with key that consist of name and phone number.
        String verificationCodekey = makeVerficationCodeKey(name, phoneNumber);

        redisRepository.insertKeyValue(verificationCodekey, verificationCode, codeTtl);
    }

    public String verifyCodeAndReturnToken(String name, String phonNumber, String verificationCode) {
        // verify code
        String verificationCodeKey = makeVerficationCodeKey(name, phonNumber);
        String returnedValue = redisRepository.getValueByKey(verificationCodeKey);

        if(returnedValue == null || !returnedValue.equals(verificationCode)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid verification code value");
        }

        // delete verified code
        redisRepository.deleteValueByKey(verificationCodeKey);

        // create, store and return the token
        // Key - token, Value - user info
        String smsVerifiedToken = makeSmsVerifiedToken();
        String smsVerifiedTokenValue = makeSmsVerifiedTokenValue(name, phonNumber);

        redisRepository.insertKeyValue(smsVerifiedToken, smsVerifiedTokenValue, tokenTtl);

        return smsVerifiedToken;
    }

    // Utils
    //

    private String makeVerficationCodeKey(String name, String phoneNumber) {
        return name + ":" + phoneNumber + ":" + "code";
    }

    private final char[] num = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private String makeVerificationCode() {
        StringBuilder stringBuilder = new StringBuilder();

        int verificationCodeLength = 6;
        for (int i = 0; i < verificationCodeLength; i++)
            stringBuilder.append(num[(int) Math.floor(Math.random() * 10)]);

        return stringBuilder.toString();
    }

    private String makeSmsVerifiedTokenValue(String name, String phoneNumber) {
        return name + ":" + phoneNumber + ":" + "token";
    }

    private String makeSmsVerifiedToken() {
        return UUID.randomUUID().toString();
    }
}
