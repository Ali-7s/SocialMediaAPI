package dev.ali.socialmediaapi.repository;

import dev.ali.socialmediaapi.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :ids")
    Page<Post> findAllByIdIn(List<Long> ids, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.id IN :ids")
    Page<Post> findAllByPostIds(List<Long> ids, Pageable pageable);

}
