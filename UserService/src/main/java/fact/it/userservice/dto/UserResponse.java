package fact.it.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String profilePicture;
    private String bio;
}
