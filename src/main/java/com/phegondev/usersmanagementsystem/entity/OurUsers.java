package com.phegondev.usersmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "ourusers")
@Data
public class OurUsers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String name;
    private String password;
    private String city;
    private String image;
    private Long numTel;
    private Long CIN;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING; // Par défaut, un MODERATOR est "PENDING"
    @JsonIgnore
    @OneToMany(mappedBy = "moderator")
    private List<SujetPfe> moderatedSujets; // Liste des sujets PFE dont l'utilisateur est le MODERATOR
    @JsonIgnore
    @OneToOne(mappedBy = "userAttribue")
    private SujetPfe attributedSujet; // Liste des sujets PFE attribués à l'utilisateur
    @JsonIgnore
    @ManyToMany(mappedBy = "demandeurs")
    private List<SujetPfe> sujetsPostules; // Liste des sujets postulés par l'utilisateur
//
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (role == UserRole.ADMIN) return true;
        return status == UserStatus.APPROVED;
    }
}
