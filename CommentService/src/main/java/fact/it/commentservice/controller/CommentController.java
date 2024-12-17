package fact.it.commentservice.controller;

import fact.it.commentservice.dto.CommentDto;
import fact.it.commentservice.dto.CommentResponseDto;
import fact.it.commentservice.model.Comment;
import fact.it.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/enriched")
    public ResponseEntity<List<CommentResponseDto>> getEnrichedComments(@RequestParam(required = false) String recipeId) {
        if (recipeId != null && !recipeId.isBlank()) {
            return ResponseEntity.ok(commentService.getCommentsResponseByRecipeId(recipeId));
        } else {
            return ResponseEntity.ok(commentService.getAllCommentResponses());
        }
    }

    @GetMapping("/raw")
    public ResponseEntity<List<Comment>> getRawComments(@RequestParam(required = false) String recipeId) {
        if (recipeId != null && !recipeId.isBlank()) {
            return ResponseEntity.ok(commentService.getCommentsByRecipeId(recipeId));
        } else {
            return ResponseEntity.ok(commentService.getAllComments());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable String id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/enriched")
    public ResponseEntity<CommentResponseDto> getEnrichedCommentById(@PathVariable String id) {
        return commentService.getCommentResponseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.createComment(commentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable String id, @RequestBody CommentDto commentDto) {
        Comment updatedComment = commentService.updateComment(id, commentDto);
        return updatedComment != null ? ResponseEntity.ok(updatedComment) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
