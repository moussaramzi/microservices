package fact.it.recipeservice.controller;

import fact.it.recipeservice.dto.RecipeDto;
import fact.it.recipeservice.dto.RecipeResponseDto;
import fact.it.recipeservice.model.Recipe;
import fact.it.recipeservice.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag
    ) {
        if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(recipeService.findByCategory(category));
        } else if (tag != null && !tag.isBlank()) {
            return ResponseEntity.ok(recipeService.findByTag(tag));
        } else {
            return ResponseEntity.ok(recipeService.getAllRecipes());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeWithAuthor(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDto recipeDto) {
        return ResponseEntity.ok(recipeService.createRecipe(recipeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody RecipeDto recipeDto) {
        Recipe updated = recipeService.updateRecipe(id, recipeDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
