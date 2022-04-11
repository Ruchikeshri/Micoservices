package com.example.myproj.userservice.Service;

import com.example.myproj.userservice.Exception.UserAlreadyExistsException;
import com.example.myproj.userservice.Exception.UserNotFoundException;
import com.example.myproj.userservice.model.RegisterAndLogin;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface UserService {

   RegisterAndLogin saveUser(RegisterAndLogin user)throws UserAlreadyExistsException;

   List<RegisterAndLogin> getAllUser();
   Optional<RegisterAndLogin> FindByEmail(String email) throws UserNotFoundException;
   RegisterAndLogin DeleteByemail(String email)throws UserNotFoundException;
   RegisterAndLogin UpdateEmail(RegisterAndLogin user1);
   RegisterAndLogin findByEmailAndPassword(String email, String password) throws UserNotFoundException;

}
