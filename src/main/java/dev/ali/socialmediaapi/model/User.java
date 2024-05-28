package dev.ali.socialmediaapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

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
    private String username;
    private String displayName;
    @JsonIgnore
    private String password;
    @Column(unique = true)
    private String email;
    private Role role;
    private Date createdAt;
    private String photoUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;

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

}
