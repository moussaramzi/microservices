package fact.it.recipeservice.service;

import fact.it.recipeservice.dto.RecipeDto;
import fact.it.recipeservice.dto.RecipeResponseDto;
import fact.it.recipeservice.dto.UserDto;
import fact.it.recipeservice.model.Recipe;
import fact.it.recipeservice.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final WebClient webClient; // Use WebClient instead of RestTemplate

    // Base URL of the UserService (adjust if needed)
    private final String userServiceBaseUrl = "http://localhost:8080/users";

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findByCategory(String category) {
        return recipeRepository.findByCategory(category);
    }

    public List<Recipe> findByTag(String tag) {
        return recipeRepository.findByTags(tag);
    }

    public Optional<RecipeResponseDto> getRecipeWithAuthor(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    UserDto userDto = fetchUserById(recipe.getAuthorId());
                    return mapToResponseDto(recipe, userDto);
                });
    }

    public Recipe createRecipe(RecipeDto recipeDto) {
        Recipe recipe = mapDtoToRecipe(recipeDto);
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Long id, RecipeDto recipeDto) {
        return recipeRepository.findById(id)
                .map(existingRecipe -> {
                    updateRecipeFromDto(existingRecipe, recipeDto);
                    return recipeRepository.save(existingRecipe);
                })
                .orElse(null);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    private UserDto fetchUserById(String userId) {
        return webClient.get()
                .uri(userServiceBaseUrl + "/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    private Recipe mapDtoToRecipe(RecipeDto dto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        recipe.setIngredients(dto.getIngredients());
        recipe.setSteps(dto.getSteps());
        recipe.setAuthorId(dto.getAuthorId());
        recipe.setCategory(dto.getCategory());
        recipe.setTags(dto.getTags());
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        return recipe;
    }

    private void updateRecipeFromDto(Recipe recipe, RecipeDto dto) {
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        recipe.setIngredients(dto.getIngredients());
        recipe.setSteps(dto.getSteps());
        recipe.setAuthorId(dto.getAuthorId());
        recipe.setCategory(dto.getCategory());
        recipe.setTags(dto.getTags());
        recipe.setUpdatedAt(LocalDateTime.now());
    }

    private RecipeResponseDto mapToResponseDto(Recipe recipe, UserDto userDto) {
        RecipeResponseDto dto = new RecipeResponseDto();
        dto.setId(recipe.getId());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setIngredients(recipe.getIngredients());
        dto.setSteps(recipe.getSteps());
        dto.setAuthor(userDto);
        dto.setCategory(recipe.getCategory());
        dto.setTags(recipe.getTags());
        dto.setCreatedAt(recipe.getCreatedAt());
        dto.setUpdatedAt(recipe.getUpdatedAt());
        return dto;
    }
}
