package fact.it.userservice.service;

import fact.it.userservice.dto.UserDto;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @PostConstruct
    public void loadData() {
        if (userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("john_doe");
            user1.setEmail("john@example.com");
            user1.setPassword("password123");
            user1.setProfilePicture("https://example.com/john.jpg");
            user1.setBio("Love cooking Italian food!");
            user1.setCreatedAt(LocalDateTime.now());
            user1.setUpdatedAt(LocalDateTime.now());

            User user2 = new User();
            user2.setUsername("jane_doe");
            user2.setEmail("jane@example.com");
            user2.setPassword("password123");
            user2.setProfilePicture("https://example.com/jane.jpg");
            user2.setBio("Spicy food enthusiast!");
            user2.setCreatedAt(LocalDateTime.now());
            user2.setUpdatedAt(LocalDateTime.now());

            userRepository.saveAll(List.of(user1, user2));
        }
    }

    public void createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setProfilePicture(userDto.getProfilePicture());
        user.setBio(userDto.getBio());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return mapUserToResponseDto(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToResponseDto)
                .toList();
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    private UserResponse mapUserToResponseDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .bio(user.getBio())
                .build();
    }
}
