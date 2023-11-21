package com.experis.course.springlamiapizzeriacrud.api;

import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNameUniqueException;
import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import com.experis.course.springlamiapizzeriacrud.model.Pizza;
import com.experis.course.springlamiapizzeriacrud.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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


    //http://localhost:8080/api/v1/pizzas
    @GetMapping
    public List<Pizza> index (@RequestParam Optional<String> search){
        return pizzaService.getPizzaList(search);
    }

    //http://localhost:8080/api/v1/pizzas/id
    @GetMapping("/{id}")
    public Pizza details(@PathVariable Integer id) {
        try {
            return pizzaService.getPizzaById(id);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //http://localhost:8080/api/v1/pizzas
    @PostMapping
    public Pizza create(@Valid @RequestBody Pizza book) {
        try {
            return pizzaService.createPizza(book);
        } catch (PizzaNameUniqueException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    //http://localhost:8080/api/v1/pizzas/id
    @PutMapping("/{id}")
    public Pizza update(@PathVariable Integer id, @Valid @RequestBody Pizza book) {
        book.setId(id);
        try {
            return pizzaService.editPizza(book);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //http://localhost:8080/api/v1/pizzas/id
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        try {
            Pizza bookToDelete = pizzaService.getPizzaById(id);
            pizzaService.deletePizza(id);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //Paginazione
    @GetMapping("/page")
    public Page<Pizza> pagedIndex(
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {

        return pizzaService.getPage(PageRequest.of(page, size));
    }

    //http://localhost:8080/api/v1/pizzas/page/v2
    @GetMapping("/page/v2")
    public Page<Pizza> pagedIndexV2(@PageableDefault(page = 0, size = 5) Pageable pageable) {
        return pizzaService.getPage(pageable);
    }
}
