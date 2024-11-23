package dev.ali.socialmediaapi.controller;

import dev.ali.socialmediaapi.dto.CreatePostRequest;
import dev.ali.socialmediaapi.dto.PostDTO;
import dev.ali.socialmediaapi.dto.UpdatePostRequest;
import dev.ali.socialmediaapi.model.ApiResponse;
import dev.ali.socialmediaapi.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static dev.ali.socialmediaapi.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getAll(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<PostDTO> posts = postService.getPosts(page);

        return new ResponseEntity<>(getResponse(Map.of("posts", posts), "All posts successfully retrieved."), HttpStatus.OK);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getPostsByUserId(@PathVariable Long userId, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<PostDTO> posts = postService.findPostsByUserId(userId, page);
        return new ResponseEntity<>(getResponse(Map.of("posts", posts), "All posts by user successfully retrieved."), HttpStatus.OK);
    }

    @GetMapping("/user/following")
    public ResponseEntity<ApiResponse> getPostsByUsersFollowing(@RequestParam(value = "page", defaultValue = "1") int page, Authentication authentication) {
        Page<PostDTO> posts = postService.findAllByUserFollowing(authentication, page);
        return new ResponseEntity<>(getResponse(Map.of("posts", posts), "All posts by user successfully retrieved."), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ApiResponse> createPost(@RequestBody CreatePostRequest postRequest, Authentication authentication) {
        PostDTO post = postService.addPost(postRequest.content(), authentication.getName());
        return new ResponseEntity<>(getResponse(Map.of("post", post), "Post successfully created."), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest postRequest, Authentication authentication) {
        PostDTO post = postService.updatePost(id, postRequest.content(), authentication.getName());
        return new ResponseEntity<>(getResponse(Map.of("post", post), "Post successfully updated."), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long id, Authentication authentication) {
        postService.deletePost(id, authentication.getName());
        return new ResponseEntity<>(getResponse(Collections.emptyMap(), "Post successfully deleted."), HttpStatus.NO_CONTENT);
    }


    @PutMapping("/{id}/like")
    public ResponseEntity<ApiResponse> likePost(@PathVariable Long id, Authentication authentication) {
        postService.likePost(id, authentication.getName());
        return new ResponseEntity<>(getResponse(Collections.emptyMap(), "Post successfully liked."), HttpStatus.OK);
    }

    @PutMapping("/{id}/unlike")
    public ResponseEntity<ApiResponse> unlikePost(@PathVariable Long id, Authentication authentication) {
        postService.unlikePost(id, authentication.getName());
        return new ResponseEntity<>(getResponse(Collections.emptyMap(), "Post successfully unliked."), HttpStatus.OK);
    }

    @GetMapping("/likes")
    public ResponseEntity<ApiResponse> getLikedPost(Authentication authentication, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<PostDTO> posts = postService.getLikedPosts(authentication.getName(), page);
        return new ResponseEntity<>(getResponse(Map.of("posts", posts), "Post successfully retrieved."), OK);
    }

    @GetMapping("/likes/ids")
    public ResponseEntity<ApiResponse> getLikedPostId(Authentication authentication) {
        List<Long> posts = postService.getLikedPostsId(authentication.getName());
        return new ResponseEntity<>(getResponse(Map.of("posts", posts), "Post successfully retrieved."), OK);
    }

}

