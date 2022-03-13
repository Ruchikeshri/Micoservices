package com.example.myproj.Service;

import java.util.List;

import com.example.myproj.Exception.BlogAlreadyExistsException;
import com.example.myproj.Exception.BlogNotFoundException;
import com.example.myproj.model.Blog;

public interface BlogService {
	
	Blog SaveBlog(Blog blog) throws BlogAlreadyExistsException;
	
	List<Blog> getAllBlogs();
	
	Blog getBlogById(int id) throws BlogNotFoundException;
	
	Blog deleteById(int id) throws  BlogNotFoundException;
	
	Blog updateBlog(Blog blog) throws BlogAlreadyExistsException,BlogNotFoundException;
	

}
