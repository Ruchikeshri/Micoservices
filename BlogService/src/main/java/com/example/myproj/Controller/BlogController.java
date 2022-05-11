package com.example.myproj.Controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
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
import com.example.myproj.model.Type;
import com.example.myproj.userservice.model.RegisterAndLogin;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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
    @Autowired
    private ExecutorService traceableExecutorService;
    private int attempt=1;
    public static final String BLOG_SERVICE="blog-service";
    public static final String USER_SERVICE="user-service";
    @Autowired
    private Resilience4JCircuitBreakerFactory circuitBreakerFactory;


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
@PostMapping("postComment")
public ResponseEntity<?> postLogged(@RequestBody Type[] type,@RequestParam String email,@RequestParam String PostId)
{
    RegisterAndLogin user = new RegisterAndLogin();
    for(int i=0;i<type.length;i++)
    {
        user = (RegisterAndLogin) blogService.addLoggedComment(type[i],email,PostId);
    }
    return new ResponseEntity<>(user,HttpStatus.OK);
}
    //    @PostMapping("/user1")
//    public List<RegisterAndLogin> adduser(@RequestBody RegisterAndLogin user)
//    {
//        String url="http://USER-SERVICE/api/v1/user";
//
//        return (List<RegisterAndLogin>) template.postForObject(url,user,RegisterAndLogin.class);
//    }
//    @GetMapping("/user1")
@GetMapping("use")
    public String getUsersthroughwebclient() {
        webClient.build()
                .get()
                .uri("http://USER-SERVICE/api/v1/users")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "GET USER FROM BLOG";
    }


//    @ApiOperation(value = "get",authorizations={@Authorization(value = "${jwt.secret}")})
    @GetMapping("blogs")
    public ResponseEntity<List<Blog>> getBlogs() {
        logger.info("Getting blog details");
        return new ResponseEntity<List<Blog>>((List<Blog>) blogService.getAllBlogs(), HttpStatus.OK);
    }
    @GetMapping("/blog1-service")
//    @CircuitBreaker(name = BLOG_SERVICE, fallbackMethod ="GetBlogs")
    @Retry(name = BLOG_SERVICE, fallbackMethod ="GetBlogs")
//    @CircuitBreaker(name = BLOG_SERVICE, fallbackMethod ="callMe")
    public ResponseEntity<?> invokeUserByEmail(@RequestParam String email) {
//        http://localhost:8086/api/v1/user?email=keshri%40gmail.com
        String url = "http://USER-SERVICE/api/v1/user?email=" + email;
        String resp= template.getForObject(url, String.class);
        System.out.println("********************************************");
        System.out.println("retry method called "+attempt+++"times"+"at"+new Date());
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }
    public ResponseEntity<?>callMe(Exception e)
    {
        return new ResponseEntity<>("service is down",HttpStatus.OK);
    }

@PostMapping("blog/add")
public ResponseEntity<?> addComment(@RequestBody Type[] type) {
    Blog blog2 = new Blog();
    for (int i = 0; i < type.length; i++)
        blog2 = blogService.addComment(type[i]);
//        return new ResponseEntity<>(blog, HttpStatus.ACCEPTED);
    return new ResponseEntity<>(blog2, HttpStatus.ACCEPTED);

}
    @PutMapping("blog")
    public ResponseEntity<Blog> updateBlog(@RequestBody Blog blog) throws BlogAlreadyExistsException, BlogNotFoundException {
        logger.info("Update Blog details"+blog);
//        circuitBreakerFactory.configureCircuitBreakerRegistry((CircuitBreakerRegistry) traceableExecutorService);
        return new ResponseEntity<Blog>(blogService.updateBlog(blog), HttpStatus.OK);
    }
    @DeleteMapping("remove")
    public ResponseEntity<?> Remove(@RequestParam int blogId,@RequestParam int index)
    {
        return new ResponseEntity<>(blogService.deleteComment(blogId,index),HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateBlog(@RequestBody List<Type>[] type) {
        Blog updatedBlog = null;
        for (int i = 0; i < type.length; i++)
            updatedBlog = blogService.updateComment(type[i]);
        return new ResponseEntity<>(updatedBlog, HttpStatus.OK);

    }
//
//	@GetMapping("blog/{BlogId}")
//	public ResponseEntity<Blog> getBlogId(@PathVariable("BlogId") int blogId ) throws BlogNotFoundException
//	{
//		return new ResponseEntity<Blog>(blogService.getBlogById(blogId),HttpStatus.OK);
//	}
    @PostMapping("/userpost")
    public ResponseEntity<?> postComment(@RequestParam String email,@RequestBody RegisterAndLogin user)
    {

        return new ResponseEntity<> (blogService.PostComment(email,user),HttpStatus.OK);
    }

    @GetMapping("blog")
    @ApiOperation(value = "get by ID")

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
    public ResponseEntity<?> GetBlogs(Exception e) {
        List<Blog> b2 = blogService.getAllBlogs();
        List<String> b4 = b2.stream().sorted(Comparator.comparingInt(Blog::getBlogId)).map(Blog::getAuthorName).collect(Collectors.toList());
        return new ResponseEntity<>(b4,HttpStatus.OK);
    }
}

