package dev.ali.socialmediaapi.service;

import dev.ali.socialmediaapi.model.Post;
import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.repository.PostRepository;
import dev.ali.socialmediaapi.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Iterable<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post addPost(String content, Authentication authentication) {
        Optional<User> selectedUser = userRepository.findByEmail(authentication.getName());
        User actualUser = selectedUser.orElseThrow();
        Post post = new Post(content, actualUser);
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void updatePost(Post post) {
        postRepository.save(post);
    }

    public Iterable<Post> findByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public void addPost(Post post) {
        postRepository.save(post);
    }
}
