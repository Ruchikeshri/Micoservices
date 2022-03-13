package com.example.myproj.Service;


import com.example.myproj.model.Bsequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class BlogSequenceGenerator {
    private MongoOperations mongoOperations;
    @Autowired
    public BlogSequenceGenerator(MongoOperations mongoOperations) {
        this.mongoOperations=mongoOperations;
    }
    public long generateBlogSequence(String seq)
    {
        Bsequence counter = mongoOperations.findAndModify(query(where("_id").is(seq)),
                new Update().inc("seq",1),options().returnNew(true).upsert(true),
                 Bsequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
//        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
