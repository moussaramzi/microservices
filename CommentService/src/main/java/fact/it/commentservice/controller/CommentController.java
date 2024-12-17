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

    // Returns enriched comment data
    @GetMapping("/enriched")
    public ResponseEntity<List<CommentResponseDto>> getEnrichedComments(@RequestParam(required = false) String recipeId) {
        if (recipeId != null && !recipeId.isBlank()) {
            return ResponseEntity.ok(commentService.getCommentsResponseByRecipeId(recipeId));
        } else {
            return ResponseEntity.ok(commentService.getAllCommentResponses());
        }
    }

    // Returns raw comment data
    @GetMapping("/raw")
    public ResponseEntity<List<Comment>> getRawComments(@RequestParam(required = false) String recipeId) {
        if (recipeId != null && !recipeId.isBlank()) {
            return ResponseEntity.ok(commentService.getCommentsByRecipeId(recipeId));
        } else {
            return ResponseEntity.ok(commentService.getAllComments());
        }
    }

    // Returns a single raw comment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable String id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        return comment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Optionally, add an endpoint for a single enriched comment
    @GetMapping("/{id}/enriched")
    public ResponseEntity<CommentResponseDto> getEnrichedCommentById(@PathVariable String id) {
        return commentService.getCommentResponseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentDto commentDto) {
        Comment created = commentService.createComment(commentDto);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable String id, @RequestBody CommentDto commentDto) {
        Comment updated = commentService.updateComment(id, commentDto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
