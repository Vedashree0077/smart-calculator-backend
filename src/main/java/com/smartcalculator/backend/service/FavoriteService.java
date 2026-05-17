package com.smartcalculator.backend.service;

import com.smartcalculator.backend.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    // Save favorite expression
    Favorite save(Favorite favorite);

    // Get user favorites
    List<Favorite> getByUser(Long userId);

    // Delete favorite
    void delete(Long id);
}