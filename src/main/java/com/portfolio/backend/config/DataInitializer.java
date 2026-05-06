package com.portfolio.backend.config;

import com.portfolio.backend.model.Project;
import com.portfolio.backend.repository.Project_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final Project_Repository projectRepository;

    @Override
    public void run(String... args) {
        if (projectRepository.count() == 0) {
            Project project1 = new Project();
            project1.setTitle("Projet blog sécurisé (Java, Spring Boot, Vaadin, MySQL, Keycloak)");
            project1.setDescription("Application web de blog communautaire sécurisée avec authentification OAuth2 via Keycloak. Gestion utilisateurs, articles, commentaires, rôles (admin/utilisateur). Interface Vaadin fluide.");
            project1.setTech("Java, Spring Boot, Vaadin, MySQL, Keycloak, OAuth2, Spring Security, REST API, CompletableFuture");
            project1.setGithubLink("https://github.com/amarwade/projet-blog-fin-d-etude");

            Project project2 = new Project();
            project2.setTitle("Application de gestion de dépenses (Java - Console)");
            project2.setDescription("Application console en Java pour gérer les dépenses personnelles avec persistance des données.");
            project2.setTech("Java, Console, POO, File I/O");
            project2.setGithubLink("https://github.com/amarwade/gestion-depenses");

            projectRepository.save(project1);
            projectRepository.save(project2);
            System.out.println("✅ Données initiales insérées dans la table project");
        } else {
            System.out.println("ℹ️ La table project contient déjà des données");
        }
    }
}
