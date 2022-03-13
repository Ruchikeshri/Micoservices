package com.example.myproj.userservice.Repository;

import com.example.myproj.userservice.model.RegisterAndLogin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<RegisterAndLogin,String> {
}
