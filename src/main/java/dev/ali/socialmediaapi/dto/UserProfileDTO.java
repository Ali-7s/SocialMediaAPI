package dev.ali.socialmediaapi.dto;

import dev.ali.socialmediaapi.model.Role;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String username;
    private Role role;
    private String displayName;
    private Set<UserSummaryDTO> followers;
    private Set<UserSummaryDTO> following;
    private List<PostDTO> posts;
    private String photoUrl;
}


