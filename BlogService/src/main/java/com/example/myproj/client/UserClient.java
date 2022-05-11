package com.example.myproj.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {
//http://localhost:8086/api/v1/UsersID
    @GetMapping("/api/v1/users")
    public String getUsers();
    @GetMapping("api/v1/user?email=")
    public String getUser(@RequestParam String email);


}
