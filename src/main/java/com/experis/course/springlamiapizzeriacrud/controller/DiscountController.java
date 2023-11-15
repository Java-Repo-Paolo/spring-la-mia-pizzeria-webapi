package com.experis.course.springlamiapizzeriacrud.controller;

import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import com.experis.course.springlamiapizzeriacrud.model.Discount;
import com.experis.course.springlamiapizzeriacrud.model.Pizza;
import com.experis.course.springlamiapizzeriacrud.repository.DiscountRepository;
import com.experis.course.springlamiapizzeriacrud.repository.PizzaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private PizzaRepository pizzaRepository;

    @GetMapping("/create")
    public String create(@RequestParam Integer pizzaId, Model model){

        Pizza pizza = pizzaRepository.
                findById(pizzaId)
                .orElseThrow(() -> new PizzaNotFoundException("Pizza not found"));
        // Crea un nuovo sconto e collega l'istanza di pizza ad esso
        Discount discount = new Discount();
        discount.setPizza(pizza);

        model.addAttribute("discount", discount);
        return "discounts/form";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("discount") Discount formDiscount,
                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult);
            return "discounts/form";
        }
        Discount savedDiscount = discountRepository.save(formDiscount);
        return "redirect:/pizzas/show/"  + formDiscount.getPizza().getId();
    }
}
