package com.phegondev.usersmanagementsystem.service;

import com.phegondev.usersmanagementsystem.entity.SujetPfe;

import java.util.List;

public interface ISujetPfeService {
    SujetPfe ajouterSujet(SujetPfe sujetPfe);
    List<SujetPfe> getAllSujets();
    SujetPfe getSujetById(Integer id);
    SujetPfe modifierSujet(Integer id, SujetPfe updatedSujet);
    void supprimerSujet(Integer id);
}
