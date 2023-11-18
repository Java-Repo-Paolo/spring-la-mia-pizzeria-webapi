package com.experis.course.springlamiapizzeriacrud.service;

import com.experis.course.springlamiapizzeriacrud.model.Ingredient;
import com.experis.course.springlamiapizzeriacrud.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    public List<Ingredient> getAll() {
        return ingredientRepository.findByOrderByName();
    }
}
