package com.example.myproj.userservice.config;

import com.example.myproj.userservice.model.RegisterAndLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class JWTTokenGeneratorImpl implements JWTTokenGenerator {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${app.jwttoken.message}")

    private String message;
    @Override
    public Map<String, String> generateToken(RegisterAndLogin user) {
        String jwtToken="";
        jwtToken = Jwts.builder().setSubject(user.getEmail()).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
//        jwtToken = Jwts.builder().setSubject(user.getId()).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, secret).compact();

        Map<String,String> jwtTokenMap = new HashMap<>();
        jwtTokenMap.put("token",jwtToken);
        jwtTokenMap.put("message",message);
        return jwtTokenMap;
    }
}
