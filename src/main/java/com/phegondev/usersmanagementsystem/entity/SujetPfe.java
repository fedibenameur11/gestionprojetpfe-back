package com.phegondev.usersmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SujetPfe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titre;
    private String description;
    private String technologie;
    private String image;
    private String rapport;
    @Enumerated(EnumType.STRING)
    private DemandeStatus demandeStatus; // PENDING, ACCEPTED, REJECTED


    @ManyToOne
    private OurUsers moderator; // Le MODERATOR responsable du sujet

    @JsonIgnore
    @ManyToOne
    private OurUsers userAttribue; // L'utilisateur attribué au sujet
    @JsonIgnore
    @ManyToMany() // mappedBy fait référence à la variable dans l'entité `OurUsers`
    private List<OurUsers> demandeurs; // Liste des demandeurs


}
