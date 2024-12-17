package fact.it.commentservice.service;

import fact.it.commentservice.dto.CommentDto;
import fact.it.commentservice.dto.CommentResponseDto;
import fact.it.commentservice.dto.RecipeDto;
import fact.it.commentservice.dto.UserDto;
import fact.it.commentservice.model.Comment;
import fact.it.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final WebClient webClient;

    // Adjust these base URLs as per your actual services
    private final String userServiceBaseUrl = "http://localhost:8080/users";
    private final String recipeServiceBaseUrl = "http://localhost:8082/api/recipes";

    /**
     * Fetch a UserDto by userId, blocking for the result.
     */
    private UserDto getUserById(String userId) {
        return webClient.get()
                .uri("http://localhost:8080/users" , uriBuilder -> uriBuilder.queryParam("id", userId).build())
                .retrieve()
                .bodyToMono(UserDto.class)
                .block(); // blocking call, like in your other project
    }

    /**
     * Fetch a RecipeDto by recipeId, blocking for the result.
     */
    private RecipeDto getRecipeById(String recipeId) {
        return webClient.get()
                .uri("http://localhost:8082/api/recipes" , uriBuilder -> uriBuilder.queryParam("id", recipeId).build())
                .retrieve()
                .bodyToMono(RecipeDto.class)
                .block(); // blocking call, like in your other project
    }

    /**
     * Map a Comment to a CommentResponseDto by fetching user and recipe data.
     */
    private CommentResponseDto mapToResponseDto(Comment comment) {
        UserDto user = getUserById(comment.getUserId());
        RecipeDto recipe = getRecipeById(comment.getRecipeId());

        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setUser(user);
        dto.setRecipe(recipe);

        return dto;
    }

    public List<CommentResponseDto> getCommentsResponseByRecipeId(String recipeId) {
        List<Comment> comments = commentRepository.findByRecipeId(recipeId);
        return comments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getAllCommentResponses() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<CommentResponseDto> getCommentResponseById(String id) {
        return commentRepository.findById(id).map(this::mapToResponseDto);
    }



    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(String id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getCommentsByRecipeId(String recipeId) {
        return commentRepository.findByRecipeId(recipeId);
    }

    public Comment createComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setUserId(commentDto.getUserId());
        comment.setRecipeId(commentDto.getRecipeId());
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    public Comment updateComment(String id, CommentDto commentDto) {
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isPresent()) {
            Comment comment = existingComment.get();
            comment.setContent(commentDto.getContent());
            comment.setUpdatedAt(LocalDateTime.now());
            return commentRepository.save(comment);
        } else {
            return null;
        }
    }
}