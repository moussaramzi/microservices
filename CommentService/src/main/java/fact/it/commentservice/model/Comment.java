package fact.it.commentservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comments")
@Data
public class Comment {
    @Id
    private String id;
    private String userId;
    private String recipeId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}