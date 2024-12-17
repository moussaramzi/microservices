package fact.it.commentservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String profilePicture;
    private String bio;
}