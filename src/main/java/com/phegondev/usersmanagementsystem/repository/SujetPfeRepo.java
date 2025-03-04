package com.phegondev.usersmanagementsystem.repository;

import com.phegondev.usersmanagementsystem.entity.SujetPfe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SujetPfeRepo extends JpaRepository<SujetPfe, Integer> {
    List<SujetPfe> findByUserAttribueId(Integer userId);
    List<SujetPfe> findByDemandeurs_Id(Integer userId);
    List<SujetPfe> findByModeratorId(Integer moderatorId);
}
