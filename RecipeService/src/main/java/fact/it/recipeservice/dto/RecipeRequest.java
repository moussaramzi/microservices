package fact.it.recipeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {
    private String title;
    private String description;
    private List<String> ingredients;
    private List<String> steps;
    private String authorId;
    private String category;
    private List<String> tags;
}
