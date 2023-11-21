package com.experis.course.springlamiapizzeriacrud.service;

import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNameUniqueException;
import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import com.experis.course.springlamiapizzeriacrud.model.Pizza;
import com.experis.course.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {
    @Autowired
    private PizzaRepository pizzaRepository;

    public List<Pizza> getPizzaList(Optional<String> search){
        if (search.isPresent()){
            return pizzaRepository.findByNameContainingIgnoreCase(search.get());
        }else {
            return pizzaRepository.findAll();
        }
    }

    public Pizza getPizzaById(Integer id) throws PizzaNotFoundException{
        Optional<Pizza> result = pizzaRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pizza with id " + id + " not found");
        }
    }

    public Pizza createPizza(Pizza pizza) throws PizzaNameUniqueException {
        pizza.setId(null);
        try{
            return pizzaRepository.save(pizza);
        } catch (RuntimeException e) {
            throw new PizzaNameUniqueException(pizza.getName());
        }
    }

    public Pizza editPizza(Pizza pizza) throws PizzaNotFoundException {
        Pizza pizzaToEdit = getPizzaById(pizza.getId());

        pizzaToEdit.setName(pizza.getName());
        pizzaToEdit.setPrice(pizza.getPrice());
        pizzaToEdit.setImage(pizza.getImage());
        pizzaToEdit.setDescription(pizza.getDescription());
        pizzaToEdit.setIngredients(pizza.getIngredients());

        return pizzaRepository.save(pizzaToEdit);
    }

    public void deletePizza (Integer id){
        pizzaRepository.deleteById(id);
    }
}
