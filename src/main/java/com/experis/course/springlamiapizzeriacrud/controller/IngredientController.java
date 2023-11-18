package com.experis.course.springlamiapizzeriacrud.controller;

import com.experis.course.springlamiapizzeriacrud.exceptions.IngredientNameUniqueException;
import com.experis.course.springlamiapizzeriacrud.model.Ingredient;
import com.experis.course.springlamiapizzeriacrud.model.Pizza;
import com.experis.course.springlamiapizzeriacrud.repository.IngredientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public String index(Model model){
        List<Ingredient> ingredientList;

        ingredientList = ingredientRepository.findByOrderByName();
        model.addAttribute("ingredientList", ingredientList);

        model.addAttribute("ingredientObj", new Ingredient());

       return"ingredients/list";
    }


    @PostMapping
    public String create(@Valid @ModelAttribute("ingredientObj") Ingredient formIngredient,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        if(bindingResult.hasErrors()){
            return "ingredients/list";
        }
        String ingredientName = formIngredient.getName();
        if (ingredientRepository.existsByName(ingredientName)) {
            // Aggiungi un messaggio di errore agli attributi flash
            redirectAttributes.addFlashAttribute("errorMessage", "Ingredient with name '" + ingredientName + "' already exists.");
            return "redirect:/ingredients";
        }
        formIngredient.setName(ingredientName);
        Ingredient savedIngredient = ingredientRepository.save(formIngredient);
        return "redirect:/ingredients";
    }


    /*public Category save(Category category) throws CategoryNameUniqueException {
        // verifico che questo nome non esista già
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryNameUniqueException(category.getName());
        }
        // trasformo il nome in lowercase
        category.setName(category.getName().toLowerCase());
        // salvo su database
        return categoryRepository.save(category);
    }*/

    @PostMapping("delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes){
      Ingredient ingredientToDelete = ingredientRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ingredientRepository.deleteById(id);
        redirectAttributes.addFlashAttribute(
                "message",
                "Ingredient "
                        + ingredientToDelete.getName()
                        + " deleted!"
        );
        return "redirect:/ingredients";
    }
}
