package com.example.myproj.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Blog1")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Blog {
    @Transient
    public static final String SEQUENCE_NAME = "sequence";

    @Id
//    private String email;
    private int blogId;
    private String blogTitle;
    private String authorName;
    private String blogContent;

}
