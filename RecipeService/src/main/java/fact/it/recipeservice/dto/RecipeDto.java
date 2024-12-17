package fact.it.recipeservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDto {
    private Long id;
    private String title;
    private String description;
    private List<String> ingredients;
    private List<String> steps;
    private String authorId;
    private String category;
    private List<String> tags;
}