package com.example.myproj.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Collections;

@Document(collection ="Content")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Type {
    @Id
    private int blogId;
    private String name;
    private String comment;

}
