package com.phegondev.usersmanagementsystem.controller;

import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.entity.SujetPfe;
import com.phegondev.usersmanagementsystem.repository.UsersRepo;
import com.phegondev.usersmanagementsystem.service.SujetPfeService;
import com.phegondev.usersmanagementsystem.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sujets")
public class SujetPfeController {
    @Autowired
    private UsersRepo ourUsersRepo;

    @Autowired
    private SujetPfeService sujetPfeService;

    // Ajouter un sujet
    @PostMapping
    public SujetPfe ajouterSujet(@RequestBody SujetPfe sujetPfe) {
        return sujetPfeService.ajouterSujet(sujetPfe);
    }

    // Récupérer tous les sujets
    @GetMapping
    public List<SujetPfe> getAllSujets() {
        return sujetPfeService.getAllSujets();
    }

    // Récupérer un sujet par ID
    @GetMapping("/{id}")
    public ResponseEntity<SujetPfe> getSujetById(@PathVariable Integer id) {
        try {
            SujetPfe sujetPfe = sujetPfeService.getSujetById(id);
            return ResponseEntity.ok(sujetPfe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Modifier un sujet
    @PutMapping("/{id}")
    public SujetPfe modifierSujet(@PathVariable Integer id, @RequestBody SujetPfe updatedSujet) {
        return sujetPfeService.modifierSujet(id, updatedSujet);
    }

    // Supprimer un sujet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerSujet(@PathVariable Integer id) {
        try {
            sujetPfeService.supprimerSujet(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/affecterModerateur/{sujetPfeId}/{moderatorId}")
    public ResponseEntity<SujetPfe> affecterModerateur(
            @PathVariable Integer sujetPfeId,
            @PathVariable Integer moderatorId) {

        // Rechercher l'utilisateur avec son ID
        OurUsers moderator = ourUsersRepo.findById(moderatorId).orElse(null);
        if (moderator == null) {
            return ResponseEntity.notFound().build(); // Si l'utilisateur n'existe pas
        }

        // Appel du service pour affecter le modérateur
        SujetPfe sujetPfe = sujetPfeService.affecterModerateur(sujetPfeId, moderator);

        if (sujetPfe != null) {
            return ResponseEntity.ok(sujetPfe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/postuler/{sujetPfeId}/{userId}")
    public ResponseEntity<SujetPfe> postulerSujetPfe(@PathVariable Integer sujetPfeId, @PathVariable Integer userId) {
        SujetPfe sujetPfe = sujetPfeService.postulerSujetPfe(sujetPfeId, userId);
        if (sujetPfe != null) {
            return new ResponseEntity<>(sujetPfe, HttpStatus.CREATED);  // Si la postulation est réussie
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Si la postulation échoue
        }
    }
    @PutMapping("/accepter/{sujetPfeId}/{userId}")
    public ResponseEntity<SujetPfe> accepterPostulation(@PathVariable Integer sujetPfeId, @PathVariable Integer userId) {
        SujetPfe sujetPfe = sujetPfeService.accepterPostulation(sujetPfeId, userId);
        if (sujetPfe != null) {
            return new ResponseEntity<>(sujetPfe, HttpStatus.OK);  // Si la postulation est acceptée
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Si le sujet ou l'utilisateur n'existe pas
        }
    }

    @PutMapping("/refuser/{sujetPfeId}/{userId}")
    public ResponseEntity<SujetPfe> refuserPostulation(@PathVariable Integer sujetPfeId, @PathVariable Integer userId) {
        SujetPfe sujetPfe = sujetPfeService.refuserPostulation(sujetPfeId, userId);
        if (sujetPfe != null) {
            return new ResponseEntity<>(sujetPfe, HttpStatus.OK);  // Si la postulation est refusée
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Si le sujet ou l'utilisateur n'existe pas
        }
    }

    @GetMapping("/demandeurs/{sujetPfeId}")
    public ResponseEntity<List<OurUsers>> getDemandeurs(@PathVariable Integer sujetPfeId) {
        List<OurUsers> demandeurs = sujetPfeService.getDemandeursBySujetPfe(sujetPfeId);
        if (demandeurs != null) {
            return new ResponseEntity<>(demandeurs, HttpStatus.OK);  // Si des demandeurs sont trouvés
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Si le sujet n'existe pas
        }
    }
    //User affectes
    @GetMapping("/projets-affectes/{userId}")
    public List<SujetPfe> getProjetsAffectes(@PathVariable Integer userId) {
        return sujetPfeService.getProjetsAffectes(userId);
    }
    @GetMapping("/projets-postules/{userId}")
    public List<SujetPfe> getProjetsPostules(@PathVariable Integer userId) {
        return sujetPfeService.getProjetsPostules(userId);
    }

    @GetMapping("/sujets-non-postules/{userId}")
    public ResponseEntity<List<SujetPfe>> getSujetsNonPostules(@PathVariable Integer userId) {
        List<SujetPfe> sujets = sujetPfeService.getSujetsNonPostules(userId);
        return ResponseEntity.ok(sujets);
    }



}
