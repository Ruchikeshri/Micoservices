package com.example.myproj.userservice.Service;

import com.example.myproj.userservice.Exception.UserAlreadyExistsException;
import com.example.myproj.userservice.Exception.UserNotFoundException;
import com.example.myproj.userservice.Repository.UserRepository;
import com.example.myproj.userservice.model.RegisterAndLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;

    @Autowired
     public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }

    @Autowired
    public MongoTemplate mongoTemplate;
    @Override
    public RegisterAndLogin saveUser(RegisterAndLogin user) throws UserAlreadyExistsException {
        if (userRepository.existsById(user.getEmail())) {
            throw new UserAlreadyExistsException("UserAlreadyExists");
        } else {
            user = userRepository.save(user);
            return user;
        }
    }


//    @Override
//    public Page<RegisterAndLogin> getAllUserp(int page, int limit) {

//        Collections.sort(users,(b2,b3)->
//                b2.getEmail().compareTo(b3.getEmail()));
//        if(page>0)
//        {
//            page = page - 1;
//        }
//        Pageable pageable = (Pageable) PageRequest.of(page,limit);
//        Page<RegisterAndLogin> users;
//        users= userRepository.findAll((org.springframework.data.domain.Pageable) pageable);
//        Page usersPage = new PageImpl<RegisterAndLogin>(users, (org.springframework.data.domain.Pageable) pageable, users.size());
//        Page<RegisterAndLogin> usersPage = new PageImpl<RegisterAndLogin>(users,pageable,users.size());
//        return usersPage;
//    }

    @Override
    public List<RegisterAndLogin> getAllUser() {
        List<RegisterAndLogin> user=userRepository.findAll();
        Collections.sort(user,(b1,b2)->
                b1.getEmail().compareTo(b2.getEmail()));
//      List<String> u1=  user.stream().sorted(Comparator.comparing(RegisterAndLogin::getEmail)).map(RegisterAndLogin::getEmail).collect(Collectors.toList());
//        System.out.println(u1);
        return (List<RegisterAndLogin>) user;
    }
    @Override
    public RegisterAndLogin UpdateEmail(RegisterAndLogin user)
    {
        RegisterAndLogin email1= null;
        Optional<RegisterAndLogin> optional = userRepository.findById(user.getEmail());
        if(optional.isPresent())
        {
            RegisterAndLogin actualEmail= userRepository.findById(user.getEmail()).get();
            actualEmail.setEmail(user.getEmail());
            email1 = userRepository.save(actualEmail);
        }
        return email1;
    }

    @Override
    public Optional<RegisterAndLogin> FindByEmail(String email) throws UserNotFoundException {
//       Optional<RegisterAndLogin> optional = userRepository.findById(email);
       RegisterAndLogin user = null;
//       if(optional.isEmpty())
//       {
//           throw new UserNotFoundException("UserNotFound");
//       }
//     else
//       {
//           user = userRepository.findById(email).get();
//
//       }
//     return user;
        Optional<Optional<RegisterAndLogin>> optional =Optional.ofNullable(userRepository.findById(email));
        return optional.orElseThrow(()-> new UserNotFoundException("UserNotFound"));

    }

    @Override
    public RegisterAndLogin DeleteByemail(String email) throws UserNotFoundException {
        RegisterAndLogin user = null;
        Optional<RegisterAndLogin> optional = userRepository.findById(email);
        if(optional.isPresent())
        {
            user = userRepository.findById(email).get();
            userRepository.deleteById(email);

        }
        else
        {
            throw new UserNotFoundException("UserNotFound");
        }
        return user;
    }

//    @Override
//    public RegisterAndLogin findByEmailAndPassword(String email, String password) throws UserNotFoundException {
//        RegisterAndLogin authUser = userRepository.findByEmailAndPassword(email, password);
//        if (authUser == null) {
//            throw new UserNotFoundException("UserNoFound");
//        }
//        return authUser;
//    }

    @Override
    public RegisterAndLogin findByEmailAndPassword(String email, String password) throws UserNotFoundException {



        RegisterAndLogin authUser = userRepository.findByEmailAndPassword(email,password);
        if(authUser==null)
        {
            throw new UserNotFoundException("UserNotFoundException");

        }
        return authUser;
    }


}
