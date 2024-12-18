package fact.it.commentservice.service;

import fact.it.commentservice.dto.CommentDto;
import fact.it.commentservice.dto.CommentResponseDto;
import fact.it.commentservice.dto.RecipeDto;
import fact.it.commentservice.dto.UserDto;
import fact.it.commentservice.model.Comment;
import fact.it.commentservice.repository.CommentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import fact.it.commentservice.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final WebClient webClient;


    @PostConstruct
    public void loadData() {
        if (commentRepository.count() == 0) {
            Comment comment1 = new Comment();
            comment1.setUserId("1");
            comment1.setRecipeId("1");
            comment1.setContent("This recipe is amazing!");
            comment1.setCreatedAt(LocalDateTime.now());
            comment1.setUpdatedAt(LocalDateTime.now());

            Comment comment2 = new Comment();
            comment2.setUserId("2");
            comment2.setRecipeId("2");
            comment2.setContent("Loved the curry, very flavorful.");
            comment2.setCreatedAt(LocalDateTime.now());
            comment2.setUpdatedAt(LocalDateTime.now());

            commentRepository.saveAll(List.of(comment1, comment2));
        }
    }

    private final String userServiceBaseUrl = "http://localhost:8080/users";
    private final String recipeServiceBaseUrl = "http://localhost:8082/api/recipes";

    public List<CommentResponseDto> getCommentsResponseByRecipeId(String recipeId) {
        return commentRepository.findByRecipeId(recipeId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getAllCommentResponses() {
        return commentRepository.findAll().stream()
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
        Comment comment = mapDtoToComment(commentDto);
        return commentRepository.save(comment);
    }

    public Comment updateComment(String id, CommentDto commentDto) {
        return commentRepository.findById(id)
                .map(existingComment -> {
                    existingComment.setContent(commentDto.getContent());
                    existingComment.setUpdatedAt(LocalDateTime.now());
                    return commentRepository.save(existingComment);
                })
                .orElse(null);
    }

    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    private UserDto getUserById(String userId) {
        return webClient.get()
                .uri(userServiceBaseUrl + "/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    private RecipeDto getRecipeById(String recipeId) {
        return webClient.get()
                .uri(recipeServiceBaseUrl + "/{id}", recipeId)
                .retrieve()
                .bodyToMono(RecipeDto.class)
                .block();
    }

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

    private Comment mapDtoToComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setUserId(commentDto.getUserId());
        comment.setRecipeId(commentDto.getRecipeId());
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return comment;
    }
}
