package com.example.myproj.userservice.Controller;

import com.example.myproj.userservice.Exception.UserAlreadyExistsException;
import com.example.myproj.userservice.Exception.UserNotFoundException;
//import com.example.myproj.userservice.Service.RabbbitMqSender;

import com.example.myproj.userservice.Repository.UserRepository;
import com.example.myproj.userservice.Service.UserService;
import com.example.myproj.userservice.config.JWTTokenGenerator;
import com.example.myproj.userservice.model.RegisterAndLogin;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RefreshScope
@RequestMapping(value = "/api/v1/")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    @Autowired
    private final StreamBridge streamBridge;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private JWTTokenGenerator jwtTokenGenerator;
    ResponseEntity<?> responseEntity;

    @Autowired
    public UserController(UserService userService, StreamBridge streamBridge, JWTTokenGenerator jwtTokenGenerator) {
        this.userService = userService;
        this.streamBridge = streamBridge;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }


    @Value("${app.message}")
    private String message;
    @Value("${app.controller.exception.message1}")
    private String message1;

    @Value("${app.controller.exception.message2}")
    private String message2;

    @Value("${app.controller.exception.message3}")
    private String message3;
//    @Autowired
//    public  UserController(RabbbitMqSender rabbbitMqSender)
//    {
//        this.rabbbitMqSender = rabbbitMqSender;
//    }

    @Autowired
    public RestTemplate template;

    //    @GetMapping("/blogs-service/{BlogId}")
//    public String invokeBlogService(@PathVariable int BlogId)
//    {
//        String url="http://BLOG-SERVICE/api/v1/blog/"+BlogId;
//      return template.getForObject(url,String.class);
//    }
    @GetMapping("/blogs-service")
    public String invokeBlogService(@RequestParam int BlogId) {
        String url = "http://BLOG-SERVICE/api/v1/blog?BlogId=" + BlogId;
        return template.getForObject(url, String.class);
    }


    @DeleteMapping("/blogs-service")
    public String deleteBlogService(@RequestParam int BlogId) {
        logger.info("Deleting Blog Service from user service");
        String url = "http://BLOG-SERVICE/api/v1/blog?BlogId=" + BlogId;
        return template.getForObject(url, String.class);
    }

    @PostMapping("user")
    public ResponseEntity<RegisterAndLogin> SaveUser(@RequestBody RegisterAndLogin user) throws UserAlreadyExistsException {
        logger.info("Adding User details");
//        String pwd = user.getPassword();
//        String encryptPwd = passwordEncoder.encode(pwd);
//        user.setPassword(encryptPwd);
        RegisterAndLogin user1 = userService.saveUser(user);
streamBridge.send("notificationEventSupplier-out-0",user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //    @PostMapping("login/user")
//    public ResponseEntity<?> loginUser(@RequestBody RegisterAndLogin user) {
//        try {
//            if (user.getEmail() == null || user.getPassword() == null) {
//                throw new UserNotFoundException(message1);
//            }
//            RegisterAndLogin userDetails = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
//            if (userDetails == null) {
//                throw new UserNotFoundException(message2);
//            }
//            if (!(user.getPassword().equals(userDetails.getPassword()))) {
//                throw new UserNotFoundException(message3);
//            }
//
//            responseEntity = new ResponseEntity<>(jwtTokenGenerator.generateToken(userDetails), HttpStatus.OK);
//        } catch (UserNotFoundException e) {
//            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//        }
//        return responseEntity;
//    }
    @PostMapping("login/user")
    public ResponseEntity<?> loginUser(@RequestBody RegisterAndLogin user) {
        try {
            if (user.getEmail() == null || user.getPassword() == null) {
                throw new UserNotFoundException(message1);
            }
            RegisterAndLogin userDetails = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
            if (userDetails == null) {
                throw new UserNotFoundException(message2);
            }

            if (!(userDetails.getPassword().equals(user.getPassword()))) {
              throw new UserNotFoundException(message3);
            }
            else
            {
                responseEntity = new ResponseEntity<>(jwtTokenGenerator.generateToken(userDetails),HttpStatus.ACCEPTED);
            }
        }
        catch (Exception e)
        {
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
        return responseEntity;
    }

    @GetMapping("/users")
    public ResponseEntity<List<RegisterAndLogin>> GetAllUsers() {
        logger.info("...Fetching all USER Details");

        return new ResponseEntity<List<RegisterAndLogin>>(userService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/usersPagination")
    public ResponseEntity<List<RegisterAndLogin>> GetAllUserspagination(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "2") int limit) {

        logger.info("...Fetching all USER Details");
        Pageable paging = (Pageable) PageRequest.of(page, limit);
//        productRepository.findAll(PageRequest.of(0, count)).toList();
        List<RegisterAndLogin> user = userRepository.findAll(PageRequest.of(page, limit)).toList();
//        return new ResponseEntity<List<RegisterAndLogin>>((List<RegisterAndLogin>) userService.getAllUserp(page,limit), HttpStatus.OK);
//   Map<String,RegisterAndLogin> response = new HashMap<>();
//   response.put("Users", (RegisterAndLogin) user);
//   return new ResponseEntity<List<RegisterAndLogin>>(userRepository.findAll(PageRequest.of(page,limit)).toList(),HttpStatus.OK);
        return new ResponseEntity<List<RegisterAndLogin>>(user, HttpStatus.OK);
    }

    @GetMapping("user")
    public ResponseEntity<?> getByEmail(@RequestParam String email) throws UserNotFoundException {
        logger.info("...Getting details based on id: " + email);

        return new ResponseEntity<>(userService.FindByEmail(email), HttpStatus.OK);
    }

    //    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody RegisterAndLogin loginDetail) {
//        if(!loginDetail.getEmail().contentEquals(null) && (!loginDetail.getPassword().contentEquals(null)))
//        {
//            return new ResponseEntity<>()
//    }
//    }
    @DeleteMapping("user/{email}")
    public ResponseEntity<RegisterAndLogin> DeleteByEmail(@PathVariable("email") String email) throws UserNotFoundException {
        logger.info("...Deleting email id: " + email);
        return new ResponseEntity<>(userService.DeleteByemail(email), HttpStatus.OK);
    }

    @GetMapping("UsersID")
    public ResponseEntity<?> GetAllEmails() throws UserNotFoundException {
        logger.info("....Getting all email id as a List");
        List<RegisterAndLogin> emails = userService.getAllUser();
        List<String> e1 = emails.stream().sorted(Comparator.comparing(RegisterAndLogin::getEmail).reversed()).map(RegisterAndLogin::getEmail).collect(Collectors.toList());
        Set<String> uniqueAuthor = new HashSet<>();
//       Set<String> duplicates= e1.stream().filter(name->uniqueAuthor.add(String.valueOf(name))).collect(Collectors.toSet());
        return new ResponseEntity<>(e1, HttpStatus.OK);
    }

    @PutMapping("user")
    @ApiOperation(value = "update emails")
    public ResponseEntity<RegisterAndLogin> UpdateEmail(@RequestBody RegisterAndLogin usr) {
        logger.info("Update Blog details");
        return new ResponseEntity<RegisterAndLogin>(userService.UpdateEmail(usr), HttpStatus.OK);
    }


}
