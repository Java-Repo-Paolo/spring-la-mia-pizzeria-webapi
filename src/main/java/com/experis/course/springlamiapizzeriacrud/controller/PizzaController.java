package com.experis.course.springlamiapizzeriacrud.controller;

import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNameUniqueException;
import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import com.experis.course.springlamiapizzeriacrud.model.Ingredient;
import com.experis.course.springlamiapizzeriacrud.model.Pizza;
import com.experis.course.springlamiapizzeriacrud.repository.IngredientRepository;
import com.experis.course.springlamiapizzeriacrud.repository.PizzaRepository;
import com.experis.course.springlamiapizzeriacrud.security.DatabaseUserDetails;
import com.experis.course.springlamiapizzeriacrud.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public String index(@RequestParam Optional<String> search, Model model, Authentication authentication){

        /*
        DatabaseUserDetails user = (DatabaseUserDetails) authentication.getPrincipal();
        System.out.println(user.getUsername());
        System.out.println(user.getAuthorities());
        */

        model.addAttribute("pizzaList", pizzaService.getPizzaList(search) );
        return "/pizzas/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model){

        try {
            Pizza pizza = pizzaService.getPizzaById(id);
            model.addAttribute("pizza", pizza);
            return "pizzas/show";
        }catch (PizzaNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("pizza", new Pizza());

        List<Ingredient> ingredientList = ingredientRepository.findByOrderByName();
        model.addAttribute("ingredientList", ingredientList);
        return "/pizzas/form";

    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza,
        BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){

            List<Ingredient> ingredientList = ingredientRepository.findByOrderByName();
            model.addAttribute("ingredientList", ingredientList);

            return "/pizzas/form";
        }
        try{
            formPizza.setCreatedAt(LocalDateTime.now());
            Pizza savedPizza = pizzaService.createPizza(formPizza);
            return "redirect:/pizzas/show/" + savedPizza.getId();
        }catch (PizzaNameUniqueException e){
            bindingResult.addError(new FieldError("pizza", "name", e.getMessage(),
                    false, null, null, "This Pizza already exist" ));
            return "/pizzas/form";
        }


    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){

        // Verifico se il risultato è presente
        try {
            model.addAttribute("pizza", pizzaService.getPizzaById(id));

            List<Ingredient> ingredientList = ingredientRepository.findByOrderByName();
            model.addAttribute("ingredientList", ingredientList);

            return "/pizzas/form";
        } catch (PizzaNotFoundException e){
            // se non ho trovato la pizza sollevo un'eccezione
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pizza with id " + id + " not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String doEdit (@PathVariable Integer id,
                          @Valid @ModelAttribute("pizza") Pizza formPizza,
                          BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            List<Ingredient> ingredientList = ingredientRepository.findByOrderByName();
            model.addAttribute("ingredientList", ingredientList);

            return "/pizzas/form";
        }
        try{
            Pizza savedPizza = pizzaService.editPizza(formPizza);
            return "redirect:/pizzas/show/" + savedPizza.getId();
        }catch (PizzaNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes){

        try{
            Pizza pizzaToDelete = pizzaService.getPizzaById(id);
            pizzaService.deletePizza(id);
            redirectAttributes.addFlashAttribute(
                    "message",
                    pizzaToDelete.getName()
                            + " deleted!"
            );
            return "redirect:/pizzas";
        }catch (PizzaNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
