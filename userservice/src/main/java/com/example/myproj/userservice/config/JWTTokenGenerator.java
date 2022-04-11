package com.example.myproj.userservice.config;

import com.example.myproj.userservice.model.RegisterAndLogin;

import java.util.Map;

public interface JWTTokenGenerator {


    Map<String,String> generateToken(RegisterAndLogin user);

}
