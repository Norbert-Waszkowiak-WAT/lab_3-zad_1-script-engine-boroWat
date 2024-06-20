package pl.edu.wat.knowledge.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Data
public class Journal {
    @MongoId
    private String id;
    
    private Integer baseScore;
    private String title;
    private String issn;

    @DBRef
    private Publisher publisher;
}