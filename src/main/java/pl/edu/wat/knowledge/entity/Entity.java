package pl.edu.wat.knowledge.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.time.Instant;


@Data
public class Entity {
    @MongoId
    private String id;
    
    private Instant createDate;

}