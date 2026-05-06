package com.portfolio.backend.controller;

import com.portfolio.backend.security.JwtUtil;
import com.portfolio.backend.service.CustomUserDetailsService;
import com.portfolio.backend.service.Project_Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Project_Controller.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Project_Service projectService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getProjects_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk());
    }

    @Test
    void postProject_WithValidBody_ShouldReturnOk() throws Exception {
        String projectJson = "{\"title\": \"Demo Project\", \"description\": \"Test description\", \"tech\": \"Java, Spring Boot\", \"githubLink\": \"https://github.com/example/demo\"}";

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk());
    }
}
