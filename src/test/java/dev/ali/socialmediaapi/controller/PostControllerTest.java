package dev.ali.socialmediaapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllPosts() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/post/all").with(jwt())).andExpect(status().isOk());

    }

    @Test
    void findByPostById() {
    }

    @Test
    void findPostsByUserId() {
    }

    @Test
    void createPost() {
    }

    @Test
    void deletePost() {
    }

}