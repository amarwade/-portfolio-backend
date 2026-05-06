package com.portfolio.backend.service;

import com.portfolio.backend.model.Project;
import com.portfolio.backend.repository.Project_Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private Project_Repository repository;

    @InjectMocks
    private Project_Service service;

    @Test
    void shouldReturnAllProjects() {
        Project project = new Project(1L, "API Portfolio", "Backend Spring", "Java, Spring", "https://github.com/demo");
        when(repository.findAll()).thenReturn(List.of(project));

        List<Project> projects = service.getAllProjects();

        assertThat(projects).hasSize(1);
        assertThat(projects.get(0).getTitle()).isEqualTo("API Portfolio");
        verify(repository).findAll();
    }
}
