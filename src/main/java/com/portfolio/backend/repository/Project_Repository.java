package com.portfolio.backend.repository;

import com.portfolio.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Project_Repository extends JpaRepository<Project, Long> {
}