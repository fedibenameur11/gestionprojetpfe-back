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
    private DemandeStatus demandeStatus;


    @ManyToOne
    private OurUsers moderator;

    @JsonIgnore
    @OneToOne
    private OurUsers userAttribue;
    @JsonIgnore
    @ManyToMany()
    private List<OurUsers> demandeurs;


}
