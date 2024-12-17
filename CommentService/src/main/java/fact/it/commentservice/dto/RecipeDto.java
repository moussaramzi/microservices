package fact.it.commentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDto {
    private Long id;
    private String title;
    private String description;
    private List<String> ingredients;
    private List<String> steps;
    private String category;
    private List<String> tags;
    private String createdAt;
    private String updatedAt;
}