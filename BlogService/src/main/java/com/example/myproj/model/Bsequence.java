package com.example.myproj.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(collection = "sequence")
public class Bsequence {
    @Id
    private String id;
    private  long seq;
}
