package com.hywook4.signupin.service;

import com.hywook4.signupin.controller.users.dto.UserChangePasswordDto;
import com.hywook4.signupin.controller.users.dto.UserInfoDto;
import com.hywook4.signupin.controller.users.dto.UserSignInResponseDto;
import com.hywook4.signupin.controller.users.dto.UserSignUpDto;
import com.hywook4.signupin.repository.RedisRepository;
import com.hywook4.signupin.repository.UserRepository;
import com.hywook4.signupin.repository.dao.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

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

        // Validate sign-up data
        validateUserSignUpInfo(userSignUpDto);

        // Create idToken and save user data to DB
        String idToken = tokenService.makeIdToken();
        User user = new User(userSignUpDto.getEmail(), userSignUpDto.getNickname(), userSignUpDto.getName(),
                userSignUpDto.getPhoneNumber(), hashPassword(userSignUpDto.getPassword()), idToken);

        if (!userRepository.save(user)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert user");
        }

        // Delete verified sms token
        deleteUsedSmsVerifiedToken(smsVerifiedToken);

        return new UserInfoDto(userSignUpDto.getEmail(), userSignUpDto.getNickname(), userSignUpDto.getName(), userSignUpDto.getPhoneNumber());
    }

    public UserSignInResponseDto signInUser(String idField, String idValue, String password) {
        User user = null;
        // Find row with matching field + value
        if (idField.equals("email")) {
            user = userRepository.getUserByEmail(idValue);
        } else if (idField.equals("phone_number")) {
            user = userRepository.getUserByPhoneNumber(idValue);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only email or phone_number field is possible");
        }

        // Validate user
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user with such email or phone_number");
        }

        // Validate password
        if (!matchesPassword(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong password");
        }

        return new UserSignInResponseDto(user.getEmail(), user.getNickname(), user.getName(), user.getPhoneNumber(), tokenService.createJwtAccessToken(user.getToken()));
    }

    public void changePassword(String smsVerifiedToken, UserChangePasswordDto userChangePasswordDto) {
        // Validate Token
        validateSmsVerifiedToken(smsVerifiedToken, userChangePasswordDto.getName(), userChangePasswordDto.getPhoneNumber());

        // Validate password
        validatePassword(userChangePasswordDto.getPassword());

        // Hash password
        String hashedPassword = hashPassword(userChangePasswordDto.getPassword());

        // Change password
        if (!userRepository.updatePasswordByNameAndPhoneNumber(hashedPassword, userChangePasswordDto.getName(), userChangePasswordDto.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update password");
        }

        // Delete token
        deleteUsedSmsVerifiedToken(smsVerifiedToken);
    }

    public UserInfoDto getUserInfo(String bearerToken) {
        // Check Authorization type and extract JWT token
        if (bearerToken == null || !bearerToken.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid access token");

        String jwtToken = bearerToken.split("Bearer ", 2)[1];

        // Validate and extract idToken from JWT Token
        String idToken;
        try {
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
        String returnedValue = redisRepository.getValueByKey(smsVerifiedToken);

        if (returnedValue == null || !returnedValue.equals(smsVerifiedTokenValue)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid SMS verified token");
        }
    }

    private void deleteUsedSmsVerifiedToken(String smsVerifiedToken) {
        redisRepository.deleteValueByKey(smsVerifiedToken);
    }

    // Validation checker
    private void validateUserSignUpInfo(UserSignUpDto userSignUpDto) {
        validateEmail(userSignUpDto.getEmail());
        validateNickname(userSignUpDto.getNickname());
        validateName(userSignUpDto.getName());
        validatePhoneNumber(userSignUpDto.getPhoneNumber());
        validatePassword(userSignUpDto.getPassword());
    }

    private void validateEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(email).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email pattern");
        }
    }

    private void validateNickname(String nickname) {
        String regex = "^[A-Za-z]+$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(nickname).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nickname must consist of alphabets only");
        }
    }

    private void validateName(String name) {
        String regex = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(name).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name pattern");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        String regex = "^[0-9]*$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(phoneNumber).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must consist of numbers only");
        }
    }

    private void validatePassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(password).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be longer than 8 and consist of at least one letter and one number");
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean matchesPassword(String plainPassword, String hasedPassword){
        return BCrypt.checkpw(plainPassword, hasedPassword);
    }
}
