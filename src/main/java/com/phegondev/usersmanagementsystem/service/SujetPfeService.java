package com.phegondev.usersmanagementsystem.service;

import com.phegondev.usersmanagementsystem.entity.DemandeStatus;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.entity.SujetPfe;
import com.phegondev.usersmanagementsystem.repository.SujetPfeRepo;
import com.phegondev.usersmanagementsystem.repository.UsersRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SujetPfeService implements ISujetPfeService {


    @Autowired
    private SujetPfeRepo sujetPfeRepository;
    @Autowired
    private UsersRepo userRepository;


    @Override
    public SujetPfe ajouterSujet(SujetPfe sujetPfe) {
        return sujetPfeRepository.save(sujetPfe);
    }

    @Override
    public List<SujetPfe> getAllSujets() {
        return sujetPfeRepository.findAll();
    }

    @Override
    public SujetPfe getSujetById(Integer id) {
        return sujetPfeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet non trouvé"));
    }

    @Override
    public SujetPfe modifierSujet(Integer id, SujetPfe updatedSujet) {
        SujetPfe s =sujetPfeRepository.findById(id).orElse(null);
        updatedSujet.setId(s.getId());
        return sujetPfeRepository.save(updatedSujet);

    }

    @Override
    public void supprimerSujet(Integer id) {
        if (sujetPfeRepository.existsById(id)) {
            sujetPfeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Sujet non trouvé");
        }
    }

    public SujetPfe postulerSujetPfe(Integer sujetPfeId, Integer userId) {
        SujetPfe sujetPfe = sujetPfeRepository.findById(sujetPfeId).orElse(null);
        OurUsers user = userRepository.findById(userId).orElse(null);

        if (sujetPfe == null || user == null) {
            return null; // Retourner null si le sujet ou l'utilisateur n'existe pas
        }

        // Vérifier si l'utilisateur a déjà postulé à ce sujet
        if (sujetPfe.getDemandeurs().contains(user)) {
            throw new IllegalStateException("Vous avez déjà postulé à ce sujet.");
        }

        sujetPfe.getDemandeurs().add(user); // Ajoute le demandeur
        sujetPfe.setDemandeStatus(DemandeStatus.PENDING); // Met à jour le statut
        return sujetPfeRepository.save(sujetPfe);
    }

    @Transactional
    public SujetPfe accepterPostulation(Integer sujetPfeId, Integer userId) {
        System.out.println("🔍 Recherche du sujet ID: " + sujetPfeId);
        Optional<SujetPfe> optionalSujetPfe = sujetPfeRepository.findById(sujetPfeId);
        Optional<OurUsers> optionalUser = userRepository.findById(userId);

        if (optionalSujetPfe.isEmpty()) {
            System.out.println("❌ Sujet non trouvé !");
            return null;
        }
        if (optionalUser.isEmpty()) {
            System.out.println("❌ Utilisateur non trouvé !");
            return null;
        }

        SujetPfe sujetPfe = optionalSujetPfe.get();
        OurUsers user = optionalUser.get();

        // Vérifie si l'utilisateur a bien postulé
        if (!sujetPfe.getDemandeurs().contains(user)) {
            System.out.println("❌ L'utilisateur " + userId + " n'a pas postulé pour ce sujet !");
            return null;
        }

        // Mettre à jour l'utilisateur attribué et le statut
        sujetPfe.setDemandeStatus(DemandeStatus.ACCEPTED);
        sujetPfe.setUserAttribue(user);
        sujetPfe.getDemandeurs().remove(user);

        SujetPfe updatedSujet = sujetPfeRepository.save(sujetPfe);
        System.out.println("✅ Postulation acceptée pour : " + updatedSujet.getTitre());
        return updatedSujet;
    }
    public SujetPfe refuserPostulation(Integer sujetPfeId, Integer userId) {
        SujetPfe sujetPfe = sujetPfeRepository.findById(sujetPfeId).orElse(null);
        OurUsers user = userRepository.findById(userId).orElse(null);

        if (sujetPfe == null) {
            System.out.println("SujetPfe avec id " + sujetPfeId + " non trouvé");
        }
        if (user == null) {
            System.out.println("Utilisateur avec id " + userId + " non trouvé");
        }

        if (sujetPfe != null && user != null && sujetPfe.getDemandeurs().contains(user)) {
            // Change le statut du sujet à REJECTED pour le demandeur
            sujetPfe.getDemandeurs().remove(user);  // Retire le demandeur de la liste
            if (sujetPfe.getDemandeurs().isEmpty()) {
                sujetPfe.setDemandeStatus(DemandeStatus.REJECTED);  // Si aucun demandeur, statut REJECTED
            }
            return sujetPfeRepository.save(sujetPfe);
        }
        return null;
    }

    public SujetPfe affecterModerateur(Integer sujetPfeId, OurUsers moderator) {
        SujetPfe sujetPfe = sujetPfeRepository.findById(sujetPfeId).orElse(null);

        if (sujetPfe != null && moderator != null) {
            sujetPfe.setModerator(moderator);
            return sujetPfeRepository.save(sujetPfe);
        }
        return null;
    }

    public List<OurUsers> getDemandeursBySujetPfe(Integer sujetPfeId) {
        // Recherche le sujet par son ID
        SujetPfe sujetPfe = sujetPfeRepository.findById(sujetPfeId).orElse(null);

        // Si le sujet existe, retourne la liste des demandeurs, sinon retourne null
        if (sujetPfe != null) {
            return sujetPfe.getDemandeurs();
        }
        return null;
    }
    public List<SujetPfe> getProjetsAffectes(Integer userId) {
        return sujetPfeRepository.findByUserAttribueId(userId);
    }

    public List<SujetPfe> getProjetsPostules(Integer userId) {
        return sujetPfeRepository.findByDemandeurs_Id(userId);
    }

    public List<SujetPfe> getSujetsNonPostules(Integer userId) {
        return sujetPfeRepository.findAll().stream()
                .filter(sujet -> sujet.getDemandeurs().stream().noneMatch(user -> user.getId().equals(userId)))
                .filter(sujet -> sujet.getUserAttribue() == null) // Vérifie que personne n'a été attribué
                .collect(Collectors.toList());
    }


    public List<SujetPfe> getSujetsAffectesModerateur(Integer moderatorId) {
        return sujetPfeRepository.findByModeratorId(moderatorId);
    }



}
