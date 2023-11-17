package com.experis.course.springlamiapizzeriacrud.controller;

import com.experis.course.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import com.experis.course.springlamiapizzeriacrud.model.Discount;
import com.experis.course.springlamiapizzeriacrud.model.Pizza;
import com.experis.course.springlamiapizzeriacrud.repository.DiscountRepository;
import com.experis.course.springlamiapizzeriacrud.repository.PizzaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

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
        // Crea un nuovo sconto e collega l'istanza di pizza
        Discount discount = new Discount();

        discount.setStartDate(LocalDate.now());
        discount.setFinishDate(LocalDate.now().plusMonths(5));

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

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Optional<Discount> result = discountRepository.findById(id);
        // Verifico se il risultato è presente
        if (result.isPresent()) {
            model.addAttribute("discount", result.get());
            return "discounts/form";
        } else {
            // se non l'ho trovato sollevo un'eccezione
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "discount with id " + id + " not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String doEdit (@PathVariable Integer id,
                          @Valid @ModelAttribute("discount")Discount formDiscount,
                          BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "/discounts/form";
        }else{
           Discount discountToEdit = discountRepository.findById(id)
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

           discountToEdit.setTitle(formDiscount.getTitle());
           discountToEdit.setStartDate(formDiscount.getStartDate());
           discountToEdit.setFinishDate(formDiscount.getFinishDate());

           Discount savedDiscount = discountRepository.save(discountToEdit);
            return "redirect:/pizzas/show/" + savedDiscount.getId();
        }
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        Discount discountToDelete = discountRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        discountRepository.deleteById(id);
        redirectAttributes.addFlashAttribute(
                "message",
                "Discount "
                        + discountToDelete.getTitle()
                        + " of "
                        + discountToDelete.getPizza().getName()
                        + " deleted!"
        );
        return "redirect:/pizzas/show/"  + discountToDelete.getPizza().getId();
    }

}
