package fact.it.recipeservice.service;

import fact.it.recipeservice.dto.RecipeRequest;
import fact.it.recipeservice.dto.RecipeResponse;
import fact.it.recipeservice.dto.UserResponse;
import fact.it.recipeservice.model.Recipe;
import fact.it.recipeservice.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final WebClient.Builder webClientBuilder;

    @PostConstruct
    public void loadData() {
        if (recipeRepository.count() <= 0) {
            Recipe recipe1 = Recipe.builder()
                    .title("Spaghetti Carbonara")
                    .description("Classic Italian pasta dish.")
                    .ingredients(List.of("Spaghetti", "Eggs", "Parmesan", "Pancetta", "Black Pepper"))
                    .steps(List.of("Boil pasta", "Fry pancetta", "Mix eggs and cheese", "Combine everything"))
                    .authorId("1")
                    .category("Italian")
                    .tags(List.of("pasta", "quick", "dinner"))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            recipeRepository.save(recipe1);
        }
    }

    public void createRecipe(RecipeRequest recipeRequest) {
        Recipe recipe = Recipe.builder()
                .title(recipeRequest.getTitle())
                .description(recipeRequest.getDescription())
                .ingredients(recipeRequest.getIngredients())
                .steps(recipeRequest.getSteps())
                .authorId(recipeRequest.getAuthorId())
                .category(recipeRequest.getCategory())
                .tags(recipeRequest.getTags())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        recipeRepository.save(recipe);
    }

    public RecipeResponse getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + id));

        UserResponse userResponse = fetchUserData(recipe.getAuthorId());

        return mapToRecipeResponse(recipe, userResponse);
    }

    public List<RecipeResponse> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(recipe -> {
                    UserResponse userResponse = fetchUserData(recipe.getAuthorId());
                    return mapToRecipeResponse(recipe, userResponse);
                })
                .collect(Collectors.toList());
    }

    private RecipeResponse mapToRecipeResponse(Recipe recipe, UserResponse userResponse) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .ingredients(recipe.getIngredients())
                .steps(recipe.getSteps())
                .category(recipe.getCategory())
                .tags(recipe.getTags())
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .user(userResponse)
                .build();
    }

    private UserResponse fetchUserData(String authorId) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-service/api/users/" + authorId)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();  // Blocking call for simplicity; consider async handling in production
    }
}
