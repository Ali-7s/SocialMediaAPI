package dev.ali.socialmediaapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9._-]*$", message = "Please enter a valid username.")
    private String username;
    private String displayName;
    @JsonIgnore
    private String password;
    @Column(unique = true)
    private String email;
    private String refreshToken;
    private Role role;
    private Date createdAt;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    @JsonIgnore
    private Set<User> followers = new HashSet<>();
    @ManyToMany(mappedBy = "followers", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> following = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Post> posts;

    @ElementCollection
    private Set<Long> likedPosts;

    public User(String username, String displayName, String email, String password) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.email = email;
        this.role = Role.USER;
        this.createdAt = new Date();
    }

    public String getRole() {
        return role.toString();
    }

    public void follow(User user) {
        following.add(user);
        user.getFollowers().add(this);

    }

    public void unfollow(User user) {
        following.remove(user);
        user.getFollowers().remove(this);
    }

}
