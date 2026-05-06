package com.portfolio.backend.controller;

import com.portfolio.backend.model.Project;
import com.portfolio.backend.service.Project_Service;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class Project_Controller {

    private final Project_Service service;

    public Project_Controller(Project_Service service) {
        this.service = service;
    }

    @GetMapping
    public List<Project> getProjects() {
        return service.getAllProjects();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Project addProject(@Valid @RequestBody Project project) {
        return service.save(project);
    }
}