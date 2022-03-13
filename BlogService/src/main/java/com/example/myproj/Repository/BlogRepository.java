package com.example.myproj.Repository;

import com.example.myproj.Exception.BlogAlreadyExistsException;
import com.example.myproj.model.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends MongoRepository<Blog,Integer>{

//    @Query("{authorName : ?0}")
    @Query(value = "{authorName: ?0}")
    Blog findByName(String authorname) throws BlogAlreadyExistsException;

}
