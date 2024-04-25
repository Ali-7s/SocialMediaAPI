package dev.ali.socialmediaapi.controller;

import dev.ali.socialmediaapi.dto.CreatePostRequest;
import dev.ali.socialmediaapi.model.Post;
import dev.ali.socialmediaapi.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("all")
    public Iterable<Post> getAllPosts() {
        return postService.findAllPosts();
    }

    @GetMapping("search/{id}")
    public Post findByPostById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/user/search/{id}")
    public Iterable<Post> findPostsByUserId(@PathVariable Long id) {
        return postService.findByUserId(id);
    }

    @PostMapping("/add")
    public void createPost(@RequestBody CreatePostRequest postRequest, Authentication authentication) {
        postService.addPost(postRequest.content(), authentication);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

}
