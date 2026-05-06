package com.portfolio.backend.service;

import com.portfolio.backend.model.Project;
import com.portfolio.backend.repository.Project_Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Project_Service {

    private final Project_Repository repository;

    public Project_Service(Project_Repository repository) {
        this.repository = repository;
    }

    public List<Project> getAllProjects() {
        return repository.findAll();
    }

    public Project save(Project project) {
        return repository.save(project);
    }
}