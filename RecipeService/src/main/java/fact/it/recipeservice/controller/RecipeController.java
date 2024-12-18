package fact.it.recipeservice.controller;

import fact.it.recipeservice.dto.RecipeRequest;
import fact.it.recipeservice.dto.RecipeResponse;
import fact.it.recipeservice.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipe(@RequestBody RecipeRequest recipeRequest) {
        recipeService.createRecipe(recipeRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeResponse getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RecipeResponse> getAllRecipes() {
        return recipeService.getAllRecipes();
    }
}
