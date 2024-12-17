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

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe createRecipe(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setIngredients(recipeDto.getIngredients());
        recipe.setSteps(recipeDto.getSteps());
        recipe.setAuthorId(recipeDto.getAuthorId());
        recipe.setCategory(recipeDto.getCategory());
        recipe.setTags(recipeDto.getTags());
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Long id, RecipeDto recipeDto) {
        Optional<Recipe> existing = recipeRepository.findById(id);
        if (existing.isPresent()) {
            Recipe recipe = existing.get();
            recipe.setTitle(recipeDto.getTitle());
            recipe.setDescription(recipeDto.getDescription());
            recipe.setIngredients(recipeDto.getIngredients());
            recipe.setSteps(recipeDto.getSteps());
            recipe.setAuthorId(recipeDto.getAuthorId());
            recipe.setCategory(recipeDto.getCategory());
            recipe.setTags(recipeDto.getTags());
            recipe.setUpdatedAt(LocalDateTime.now());
            return recipeRepository.save(recipe);
        } else {
            return null;
        }
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> findByCategory(String category) {
        return recipeRepository.findByCategory(category);
    }

    public List<Recipe> findByTag(String tag) {
        return recipeRepository.findByTags(tag);
    }

    /**
     * Returns a RecipeResponseDto with author details fetched from UserService.
     */
    public Optional<RecipeResponseDto> getRecipeWithAuthor(Long id) {
        Optional<Recipe> optRecipe = recipeRepository.findById(id);
        if (optRecipe.isEmpty()) {
            return Optional.empty();
        }
        Recipe recipe = optRecipe.get();

        // Fetch user data from the UserService using WebClient
        UserDto userDto = webClient.get()
                .uri(userServiceBaseUrl + "/{id}", recipe.getAuthorId())
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();


        return Optional.of(mapToResponseDto(recipe, userDto));
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
