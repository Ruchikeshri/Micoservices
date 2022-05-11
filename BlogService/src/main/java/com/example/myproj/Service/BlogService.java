package com.example.myproj.Service;

import java.util.List;
import java.util.Optional;

import com.example.myproj.Exception.BlogAlreadyExistsException;
import com.example.myproj.Exception.BlogNotFoundException;
import com.example.myproj.model.Blog;
import com.example.myproj.model.Type;
import com.example.myproj.userservice.model.RegisterAndLogin;

public interface BlogService {
	
	Blog SaveBlog(Blog blog) throws BlogAlreadyExistsException;
	
	List<Blog> getAllBlogs();
	public RegisterAndLogin addLoggedComment(Type type,String email,String Postid) throws BlogAlreadyExistsException, BlogNotFoundException;
	Blog getBlogById(int id) throws BlogNotFoundException;
	RegisterAndLogin PostComment(String email, RegisterAndLogin blog) throws BlogNotFoundException;
	Blog deleteById(int id) throws  BlogNotFoundException;
	
	Blog updateBlog(Blog blog) throws BlogAlreadyExistsException,BlogNotFoundException;
	Blog addComment(Type type) throws BlogAlreadyExistsException,BlogNotFoundException;
   Blog deleteComment(int BlogId,int val) throws BlogNotFoundException;
   Blog updateComment(List<Type> type) throws BlogNotFoundException;
  Optional<RegisterAndLogin> getBlogId(int BlogId) throws BlogNotFoundException;
}
