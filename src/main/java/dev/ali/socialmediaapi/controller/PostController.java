package dev.ali.socialmediaapi.controller;

import dev.ali.socialmediaapi.model.Post;
import dev.ali.socialmediaapi.service.PostService;
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

    @GetMapping("/{id}")
    public Post findByPostById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/user/{id}/all")
    public Iterable<Post> findPostsByUserId(@PathVariable Long id) {
        return postService.findByUserId(id);
    }

}
