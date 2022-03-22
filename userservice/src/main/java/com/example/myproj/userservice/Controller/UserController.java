package com.example.myproj.userservice.Controller;

import com.example.myproj.userservice.Exception.UserAlreadyExistsException;
import com.example.myproj.userservice.Exception.UserNotFoundException;
import com.example.myproj.userservice.Service.UserService;
import com.example.myproj.userservice.model.RegisterAndLogin;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;




@RestController
@RefreshScope
@RequestMapping(value="/api/v1/")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public RestTemplate template;
//    @GetMapping("/blogs-service/{BlogId}")
//    public String invokeBlogService(@PathVariable int BlogId)
//    {
//        String url="http://BLOG-SERVICE/api/v1/blog/"+BlogId;
//      return template.getForObject(url,String.class);
//    }
@GetMapping("/blogs-service")
public String invokeBlogService(@RequestParam int BlogId)
{
    String url="http://BLOG-SERVICE/api/v1/blog?BlogId="+BlogId;
    return template.getForObject(url,String.class);
}

    @DeleteMapping("/blogs-service")
    public String deleteBlogService(@RequestParam int BlogId)
    {
        logger.info("Deleting Blog Service from user service");
        String url="http://BLOG-SERVICE/api/v1/blog?BlogId="+BlogId;
        return template.getForObject(url,String.class);
    }


    @PostMapping("user")
    public ResponseEntity<RegisterAndLogin> SaveUser(@RequestBody RegisterAndLogin user) throws UserAlreadyExistsException
    { logger.info("Adding User details");
       RegisterAndLogin user1 = userService.saveUser(user);
        return new ResponseEntity<>(user1, HttpStatus.OK);
    }
    @GetMapping("/users")
    public ResponseEntity<List<RegisterAndLogin>> GetAllUsers()
    { logger.info("...Fetching all USER Details");

        return new ResponseEntity<List<RegisterAndLogin>>(userService.getAllUser(),HttpStatus.OK);
    }
    @GetMapping("user")
    public ResponseEntity<?> getByEmail(@RequestParam String email) throws UserNotFoundException{
        logger.info("...Getting details based on id: "+email);
        return new ResponseEntity<>(userService.FindByEmail(email),HttpStatus.OK);
    }
//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody RegisterAndLogin loginDetail) {
//        if(!loginDetail.getEmail().contentEquals(null) && (!loginDetail.getPassword().contentEquals(null)))
//        {
//            return new ResponseEntity<>()
//    }
//    }
    @DeleteMapping("user/{email}")
    public ResponseEntity<RegisterAndLogin> DeleteByEmail(@PathVariable("email") String email) throws UserNotFoundException
    {  logger.info("...Deleting email id: "+email);
        return new ResponseEntity<>(userService.DeleteByemail(email),HttpStatus.OK);
    }
    @GetMapping("UsersID")
    public ResponseEntity<?> GetAllEmails() throws UserNotFoundException
    {
        logger.info("....Getting all email id as a List");
        List<RegisterAndLogin> emails=userService.getAllUser();
        List<String> e1 = emails.stream().sorted(Comparator.comparing(RegisterAndLogin::getEmail).reversed()).map(RegisterAndLogin::getEmail).collect(Collectors.toList());
        Set<String> uniqueAuthor= new HashSet<>();
//       Set<String> duplicates= e1.stream().filter(name->uniqueAuthor.add(String.valueOf(name))).collect(Collectors.toSet());
        return new ResponseEntity<>(e1,HttpStatus.OK);
    }

    @PutMapping("user")
    @ApiOperation(value = "update emails")
    public ResponseEntity<RegisterAndLogin> UpdateEmail(@RequestBody RegisterAndLogin usr) {
        logger.info("Update Blog details");
        return new ResponseEntity<RegisterAndLogin>(userService.UpdateEmail(usr),HttpStatus.OK);
    }


}
