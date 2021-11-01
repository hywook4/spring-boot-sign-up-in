package com.hywook4.signupin.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${auth.token.secretKey}")
    private String secretKey;

    @Value("${auth.token.expireTime}")
    private Long tokenExpireTime;

    public String createJwtAccessToken(String idToken) {
        // Token header
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // Token payload
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("idToken", idToken);

        // Token expire date
        Date expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + tokenExpireTime);

        // Build token and return
        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject("user")
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public Map<String, Object> validateJwtAndReturnPayload(String jwtToken) {
        Map<String, Object> payloads = null;
        try {
            payloads = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        return payloads;
    }

    public String makeIdToken() {
        return UUID.randomUUID().toString();
    }
}
