package com.hywook4.signupin.repository;

import com.hywook4.signupin.controller.users.dto.UserInfoDto;
import com.hywook4.signupin.repository.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean save(User user) {
        String sql = "INSERT INTO user(email, nickname, name, phone_number, password, token) VALUES (?, ?, ?, ?, ?, ?)";

        int result = jdbcTemplate.update(sql,
                user.getEmail(), user.getNickname(), user.getName(), user.getPhoneNumber(), user.getPassword(), user.getToken());

        return result == 1;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, email);

        return mapResultToUser(result);
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM user WHERE phone_number = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, phoneNumber);

        return mapResultToUser(result);
    }

    public UserInfoDto getUserInfoByToken(String token) {
        String sql = "SELECT * FROM user WHERE token = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, token);

        return mapResultToUserInfo(result);
    }

    public boolean updatePasswordByNameAndPhoneNumber(String password, String name, String phoneNumber) {
        String sql = "UPDATE user SET password=? WHERE name = ?, phone_number = ?";
        int result = jdbcTemplate.update(sql, password, name, phoneNumber);

        return result == 1;
    }


    // Utils
    //
    private User mapResultToUser(Map<String, Object> result) {
        User user = new User();
        user.setId(((Number) result.get("id")).longValue());
        user.setEmail((String) result.get("email"));
        user.setNickname((String) result.get("nickname"));
        user.setName((String) result.get("name"));
        user.setPhoneNumber((String) result.get("phone_number"));
        user.setPassword((String) result.get("password"));
        user.setToken((String) result.get("token"));

        return user;
    }

    private UserInfoDto mapResultToUserInfo(Map<String, Object> result) {
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setEmail((String) result.get("email"));
        userInfo.setNickname((String) result.get("nickname"));
        userInfo.setName((String) result.get("name"));
        userInfo.setPhoneNumber((String) result.get("phone_number"));

        return userInfo;
    }
}
