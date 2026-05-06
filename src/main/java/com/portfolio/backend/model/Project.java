package com.portfolio.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor //
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire.")
    @Size(max = 150, message = "Le titre ne doit pas depasser 150 caracteres.")
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "La description est obligatoire.")
    private String description;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Le champ tech est obligatoire.")
    private String tech;

    @Size(max = 500, message = "Le lien GitHub ne doit pas depasser 500 caracteres.")
    private String githubLink;
}