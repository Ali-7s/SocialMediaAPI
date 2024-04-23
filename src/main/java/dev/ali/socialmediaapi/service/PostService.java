package dev.ali.socialmediaapi.service;

import dev.ali.socialmediaapi.model.Post;
import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.repository.PostRepository;
import dev.ali.socialmediaapi.repository.UserRepository;
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

    public void addPost(String title, String content, User user) {
        Optional<User> selectedUser = userRepository.findByEmail(user.getEmail());

        User actualUser = selectedUser.orElseThrow();

        Post post = new Post(title, content, actualUser);

        postRepository.save(post);
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
