package com.example.myproj.Controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.myproj.Exception.BlogAlreadyExistsException;
import com.example.myproj.Exception.BlogNotFoundException;

import com.example.myproj.Service.BlogSequenceGenerator;
//import com.example.myproj.client.UserClient;
import com.example.myproj.client.UserClient;
import com.example.myproj.model.Blog;
//import com.example.myproj.userservice.model.RegisterAndLogin;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.Authorization;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.myproj.Service.BlogService;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@CrossOrigin(value = "*")
@RefreshScope
@RestController
@RequestMapping(value = "/api/v1/")
public class BlogController {
    private static Logger logger = LoggerFactory.getLogger(BlogController.class);

    private BlogService blogService;
    @Autowired
    private BlogSequenceGenerator sequenceGenerator;
//    private final UserClient userClient;
    @Autowired
    private RestTemplate template;
    @Autowired
    private WebClient.Builder webClient;
    @Autowired
    private UserClient client;
    public static final String BLOG_SERVICE="blog-service";
    @Autowired
    public BlogController(BlogService blogService) {

        this.blogService = blogService;
//        this.userClient = userClient;
    }

    @Bean
    public WebMvcConfigurer configure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("http://localhost:8086");
            }
        };
    }
@GetMapping("/findAll")
public String getAll()
{
    return client.getUsers();
}
@GetMapping("/user1")
public ResponseEntity<?> getUser(@RequestParam String email)
{
//   client.getUser();
    logger.info(client.getUser(email));
//    return client.getUser();
    return new ResponseEntity<>(client.getUser(email),HttpStatus.OK);
}
//    @ApiOperation(value = "Save the blogs")
    @PostMapping("blog")
    public ResponseEntity<Blog> SaveBlog(@RequestBody Blog blog) throws BlogAlreadyExistsException {
        logger.info("Adding Blog details");
        blog.setBlogId((int) sequenceGenerator.generateBlogSequence(Blog.SEQUENCE_NAME));
//		Blog SavedBlog=blogService.SaveBlog(blog);

        return new ResponseEntity<>(blogService.SaveBlog(blog), HttpStatus.OK);
    }
    @Value("${app.validationConfirmationMessage}")
    private String validationConfirmationMessage;

    @GetMapping("/users-service")
    public String invokeUserService() {
        String url = "http://USER-SERVICE/api/v1/users";
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("user-key" , "39f6dbeccbb0bd94593baf9a4d295c66");
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        return  template.exchange(url, HttpMethod.GET, entity,Object.class);
        return template.getForObject(url, String.class);
    }
//    @Value("${jwt.secret}")
//    @ApiOperation(value = "authorize",authorizations={@Authorization(value = "${jwt.secret}")})
    @GetMapping("data")
    public String getSensitiveData() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization" , "secret");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return validationConfirmationMessage;
    }

    //    @PostMapping("/user1")
//    public List<RegisterAndLogin> adduser(@RequestBody RegisterAndLogin user)
//    {
//        String url="http://USER-SERVICE/api/v1/user";
//
//        return (List<RegisterAndLogin>) template.postForObject(url,user,RegisterAndLogin.class);
//    }
    @PostMapping("/user1")
    public String addUser() {
        webClient.build()
                .post()
                .uri("http://USER-SERVICE/api/v1/user")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "ADDED USER FROM BLOG";
    }


//    @ApiOperation(value = "get",authorizations={@Authorization(value = "${jwt.secret}")})
    @GetMapping("blogs")
    public ResponseEntity<List<Blog>> getBlogs() {
        logger.info("Getting blog details");
        return new ResponseEntity<List<Blog>>((List<Blog>) blogService.getAllBlogs(), HttpStatus.OK);
    }

    @PutMapping("blog")
    public ResponseEntity<Blog> updateBlog(@RequestBody Blog blog) throws BlogAlreadyExistsException, BlogNotFoundException {
        logger.info("Update Blog details");
        return new ResponseEntity<Blog>(blogService.updateBlog(blog), HttpStatus.OK);
    }
//
//	@GetMapping("blog/{BlogId}")
//	public ResponseEntity<Blog> getBlogId(@PathVariable("BlogId") int blogId ) throws BlogNotFoundException
//	{
//		return new ResponseEntity<Blog>(blogService.getBlogById(blogId),HttpStatus.OK);
//	}

    @GetMapping("blog")
    @ApiOperation(value = "get by ID")
//    @CircuitBreaker(name = BLOG_SERVICE, fallbackMethod ="getBlogs")
//  @CircuitBreaker(name = BLOG_SERVICE,fallbackMethod = "getBlogs")
    public ResponseEntity<Blog> getBlogById(@RequestParam int BlogId) throws BlogNotFoundException {
        logger.info(".........Get Blog by ID details");
        return new ResponseEntity<Blog>(blogService.getBlogById(BlogId), HttpStatus.OK);
    }

    //    @GetMapping("blog/{BlogId}")
//public ResponseEntity<Blog> getBlogById(@PathVariable("BlogId") int blogId) throws BlogNotFoundException
//{
//	return new ResponseEntity<Blog>(blogService.getBlogById(blogId),HttpStatus.OK);
//}
    @DeleteMapping("blog")
//    @ApiOperation(value = "get by ID")
    public ResponseEntity<Blog> DeleteId(@RequestParam int BlogId) throws BlogNotFoundException {
        logger.info("............Delete Blog by ID details");
        return new ResponseEntity<Blog>(blogService.deleteById(BlogId), HttpStatus.OK);
    }

    @GetMapping("blogsID")
//    @ApiOperation(value = "get by ID")
    public ResponseEntity<?> GetBlogs() {
        List<Blog> b2 = blogService.getAllBlogs();
        List<String> b4 = b2.stream().sorted(Comparator.comparingInt(Blog::getBlogId)).map(Blog::getAuthorName).collect(Collectors.toList());
        return new ResponseEntity<>(String.valueOf(b4), HttpStatus.OK);
    }
}

