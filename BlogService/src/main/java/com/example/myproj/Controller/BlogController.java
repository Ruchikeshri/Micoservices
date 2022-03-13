package com.example.myproj.Controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.myproj.Exception.BlogAlreadyExistsException;
import com.example.myproj.Exception.BlogNotFoundException;

import com.example.myproj.Service.BlogSequenceGenerator;
import com.example.myproj.model.Blog;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.myproj.Service.BlogService;

//@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/api/v1/")
public class BlogController {
    private static Logger logger = LoggerFactory.getLogger(BlogController.class);

    private BlogService blogService;
    @Autowired
    private BlogSequenceGenerator sequenceGenerator;

    @Autowired
    public BlogController(BlogService blogService) {

        this.blogService = blogService;
    }

    @ApiOperation(value = "Save the blogs")
    @PostMapping("blog")
    public ResponseEntity<Blog> SaveBlog(@RequestBody Blog blog) throws BlogAlreadyExistsException {
        logger.info("Adding Blog details");
        blog.setBlogId((int) sequenceGenerator.generateBlogSequence(Blog.SEQUENCE_NAME));
//		Blog SavedBlog=blogService.SaveBlog(blog);

        return new ResponseEntity<>(blogService.SaveBlog(blog), HttpStatus.OK);
    }

//    @GetMapping("blogs")
//    @ApiOperation(value = "get")
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
    @ApiOperation(value = "get by ID")
    public ResponseEntity<Blog> DeleteId(@RequestParam int BlogId) throws BlogNotFoundException {
        logger.info("............Delete Blog by ID details");
        return new ResponseEntity<Blog>(blogService.deleteById(BlogId), HttpStatus.OK);
    }

//    @GetMapping("blogsID")
//    @ApiOperation(value = "get by ID")
//    public ResponseEntity<?> GetBlogs() {
//        List<Blog> b2 = blogService.getAllBlogs();
//        List<String> b4 = b2.stream().sorted(Comparator.comparingInt(Blog::getBlogId)).map(Blog::getAuthorName).collect(Collectors.toList());
//        return new ResponseEntity<>(String.valueOf(b4), HttpStatus.OK);
//    }
}

