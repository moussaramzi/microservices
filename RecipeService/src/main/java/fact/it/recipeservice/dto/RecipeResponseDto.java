package fact.it.recipeservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecipeResponseDto {
    private Long id;
    private String title;
    private String description;
    private List<String> ingredients;
    private List<String> steps;
    private UserDto author;
    private String category;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}