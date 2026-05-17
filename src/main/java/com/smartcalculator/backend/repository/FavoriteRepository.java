package com.smartcalculator.backend.repository;

import com.smartcalculator.backend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Get favorites by user
    List<Favorite> findByUserId(Long userId);
}