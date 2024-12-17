package fact.it.commentservice.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String userId;
    private String recipeId;
    private String content;
}