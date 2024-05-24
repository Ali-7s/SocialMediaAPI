package dev.ali.socialmediaapi.controller;
import dev.ali.socialmediaapi.dto.CreatePostRequest;
import dev.ali.socialmediaapi.dto.UpdatePostRequest;
import dev.ali.socialmediaapi.model.ApiResponse;
import dev.ali.socialmediaapi.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import static dev.ali.socialmediaapi.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse> getAllPosts() {
        return ResponseEntity.ok().body(getResponse(Map.of("posts", postService.findAllPosts()), "All posts successfully retrieved.", OK));
    }

    @GetMapping("search/{id}")
    public ResponseEntity<ApiResponse> findByPostById(@PathVariable Long id) {
        return ResponseEntity.ok().body(getResponse(Map.of("post", postService.findById(id)), "Post successfully retrieved.", OK));
    }

    @GetMapping("/user/search/{id}")
    public ResponseEntity<ApiResponse> findPostsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(getResponse(Map.of("posts", postService.findByUserId(id)), "All posts by user id successfully retrieved.", OK));

    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createPost(@RequestBody CreatePostRequest postRequest, Authentication authentication) {
        return ResponseEntity.created(URI.create("")).body(getResponse(Map.of("post", postService.addPost(postRequest.content(), authentication)), "Post successfully created.", CREATED));
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updatePost(@RequestBody UpdatePostRequest postRequest) {
        return ResponseEntity.ok().body(getResponse(Map.of("post", postService.updatePost(postRequest.id(), postRequest.content())), "Post successfully updated.", OK));    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().body(getResponse(Collections.emptyMap(), "Post successfully deleted.", NO_CONTENT));
    }

}
