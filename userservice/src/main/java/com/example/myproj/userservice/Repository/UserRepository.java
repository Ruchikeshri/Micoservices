package com.example.myproj.userservice.Repository;

import com.example.myproj.userservice.model.RegisterAndLogin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends MongoRepository<RegisterAndLogin,String> {
    @Query(value = "{email: ?0},{password: ?1}")
    public RegisterAndLogin findByEmailAndPassword(String email, String password);
}
