package com.example.myproj.Repository;

import com.example.myproj.Exception.BlogAlreadyExistsException;
import com.example.myproj.Exception.BlogNotFoundException;
import com.example.myproj.model.Blog;
import com.example.myproj.userservice.model.RegisterAndLogin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface PostRepository extends MongoRepository<RegisterAndLogin,String> {
//    @Query(value = "{BlogId: ?0}")
//@Query("SELECT blogId  FROM RegisterAndLogin ")
//@Query(value = "{blogId: ?0}")
//@Query("{ '_id.field1': ?0")
    @Query(value = "{BlogId: ?0}")
    Optional<RegisterAndLogin> findById(int blogId) throws BlogAlreadyExistsException;

//    Optional<Blog> findById(int blogId) throws BlogNotFoundException;
}
