package com.hywook4.signupin.service.user;

import com.hywook4.signupin.controller.users.dto.*;
import com.hywook4.signupin.repository.RedisRepository;
import com.hywook4.signupin.repository.UserRepository;
import com.hywook4.signupin.repository.dao.User;
import com.hywook4.signupin.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final RedisRepository redisRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Autowired
    public UserService(RedisRepository redisRepository, TokenService tokenService, UserRepository userRepository) {
        this.redisRepository = redisRepository;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public UserInfoDto signUpUser(String smsVerifiedToken, UserSignUpDto userSignUpDto) {
        // Validate token
        validateSmsVerifiedToken(smsVerifiedToken, userSignUpDto.getName(), userSignUpDto.getPhoneNumber());

        // TODO: Validate Sign up data
        // TODO: If there's time, decryt encryted password with secret key

        // Create idToken and save user data to DB
        String idToken = tokenService.makeIdToken();
        User user = new User(userSignUpDto.getEmail(), userSignUpDto.getNickname(), userSignUpDto.getName(), userSignUpDto.getPhoneNumber(), userSignUpDto.getPassword(), idToken);

        if(!userRepository.save(user)){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert user");
        }

        // Delete verified sms token
        deleteUsedSmsVerifiedToken(smsVerifiedToken);

        return new UserInfoDto(userSignUpDto.getEmail(), userSignUpDto.getNickname(), userSignUpDto.getName(), userSignUpDto.getPhoneNumber());
    }

    public UserSignInResponseDto signInUser(String idField, String idValue, String password){
        User user = null;
        // Find row with matching field + value
        if(idField.equals("email") || idField.equals("phone_number")){
            user = userRepository.getUserByEmail(idValue);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only email or phone_number field is possible");
        }

        // Validate user
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user with such email or phone_number");
        }

        // Validate password
        if (!user.getPassword().equals(password)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong password");
        }

        return new UserSignInResponseDto(user.getEmail(), user.getNickname(), user.getName(), user.getPhoneNumber(), tokenService.createJwtAccessToken(user.getToken()));
    }

    public void changePassword(String smsVerifiedToken, UserChangePasswordDto userChangePasswordDto) {
        // Validate Token
        validateSmsVerifiedToken(smsVerifiedToken, userChangePasswordDto.getName(), userChangePasswordDto.getPhoneNumber());

        // TODO: Validate password

        // Change password
        if(!userRepository.updatePasswordByNameAndPhoneNumber(userChangePasswordDto.getPassword(), userChangePasswordDto.getName(), userChangePasswordDto.getPhoneNumber())){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update password");
        }

        // Delete token
        deleteUsedSmsVerifiedToken(smsVerifiedToken);
    }

    public UserInfoDto getUserInfo(String bearerToken) {
        // Check Authorization type and extract JWT token
        if(bearerToken == null || !bearerToken.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid access token");

        String jwtToken = bearerToken.split("Bearer ", 2)[1];

        // Validate and extract idToken from JWT Token
        String idToken;
        try{
            idToken = (String) tokenService.validateJwtAndReturnPayload(jwtToken).get("idToken");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No payload found in token");
        }

        // find user by idToken.
        return userRepository.getUserInfoByToken(idToken);
    }


    // Utils
    //

    private String makeSmsVerifiedTokenValue(String name, String phoneNumber) {
        return name + ":" + phoneNumber + ":" + "token";
    }

    private void validateSmsVerifiedToken(String smsVerifiedToken, String name, String phoneNumber) {
        String smsVerifiedTokenValue = makeSmsVerifiedTokenValue(name, phoneNumber);

        if (!redisRepository.getValueByKey(smsVerifiedToken).equals(smsVerifiedTokenValue)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid SMS verified token");
        }
    }

    private void deleteUsedSmsVerifiedToken(String smsVerifiedToken) {
        redisRepository.deleteValueByKey(smsVerifiedToken);
    }
}
