package dev.ali.socialmediaapi.service;

import dev.ali.socialmediaapi.dto.PostDTO;
import dev.ali.socialmediaapi.dto.PostMapper;
import dev.ali.socialmediaapi.model.Post;
import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.repository.PostRepository;
import dev.ali.socialmediaapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Cacheable(value = "allPosts", key = "#page")
    public Page<PostDTO> getPosts(int page) {
        return postRepository.findAll(PageRequest.of(page, 5, Sort.by("createdAt").descending())).map(postMapper::toPostDTO);
    }

    //    @Cacheable(value = "userPosts", key = "#userId")
    public Page<PostDTO> findPostsByUserId(Long userId, int page) {
        return postRepository.findAllByUserId(userId, PageRequest.of(page, 5, Sort.by("createdAt").descending())).map(postMapper::toPostDTO);

    }

    public Page<PostDTO> findAllByUserFollowing(Authentication authentication, int page) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return postRepository.findAllByIdIn(user.getFollowing().stream().map(User::getId).toList(), PageRequest.of(page, 5, Sort.by("createdAt").descending())).map(postMapper::toPostDTO);

    }

    public Page<PostDTO> getLikedPosts(String email, int page) {
        User user = userRepository.findByEmail(email).orElseThrow();

        return postRepository.findAllByPostIds(user.getLikedPosts().stream().toList(), PageRequest.of(page, 5, Sort.by("createdAt").descending())).map(postMapper::toPostDTO);
    }

    public List<Long> getLikedPostsId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return user.getLikedPosts().stream().toList();
    }


    @CacheEvict(value = "allPosts", allEntries = true)
    public PostDTO addPost(String content, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));
        Post post = new Post(content, user);
        Post savedPost = postRepository.save(post);
        return postMapper.toPostDTO(savedPost);
    }

    @CacheEvict(value = "allPosts", allEntries = true)
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + postId));
        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }
        postRepository.delete(post);
    }

    @CacheEvict(value = "allPosts", allEntries = true)
    public PostDTO updatePost(Long postId, String content, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id " + postId));
        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not authorized to update this post");
        }
        post.setContent(content);
        Post updatedPost = postRepository.save(post);
        return postMapper.toPostDTO(updatedPost);
    }


    public void likePost(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Post post = postRepository.findById(id).orElseThrow();
        user.getLikedPosts().add(post.getId());
        userRepository.save(user);
        post.getUsersLikedIds().add(user.getId());
        postRepository.save(post);
    }

    public void unlikePost(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Post post = postRepository.findById(id).orElseThrow();
        user.getLikedPosts().remove(post.getId());
        userRepository.save(user);
        post.getUsersLikedIds().remove(user.getId());
        postRepository.save(post);
    }


}
