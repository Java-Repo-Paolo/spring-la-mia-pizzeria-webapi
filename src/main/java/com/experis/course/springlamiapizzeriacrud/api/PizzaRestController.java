package com.experis.course.springlamiapizzeriacrud.api;

import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNameUniqueException;
import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import com.experis.course.springlamiapizzeriacrud.model.Pizza;
import com.experis.course.springlamiapizzeriacrud.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pizzas")
@CrossOrigin
public class PizzaRestController {
    @Autowired
    private PizzaService pizzaService;


    /*http://localhost:8080/api/v1/pizzas*/
    @GetMapping
    public List<Pizza> index (@RequestParam Optional<String> search){
        return pizzaService.getPizzaList(search);
    }

    @GetMapping("/{id}")
    public Pizza details(@PathVariable Integer id) {
        try {
            return pizzaService.getPizzaById(id);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Pizza create(@Valid @RequestBody Pizza book) {
        try {
            return pizzaService.createPizza(book);
        } catch (PizzaNameUniqueException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Pizza update(@PathVariable Integer id, @Valid @RequestBody Pizza book) {
        book.setId(id);
        try {
            return pizzaService.editPizza(book);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        try {
            Pizza bookToDelete = pizzaService.getPizzaById(id);
            pizzaService.deletePizza(id);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
