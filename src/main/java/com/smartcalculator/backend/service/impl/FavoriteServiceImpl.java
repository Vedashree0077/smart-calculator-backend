package com.smartcalculator.backend.service.impl;

import com.smartcalculator.backend.entity.Favorite;
import com.smartcalculator.backend.repository.FavoriteRepository;
import com.smartcalculator.backend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public List<Favorite> getByUser(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }
}