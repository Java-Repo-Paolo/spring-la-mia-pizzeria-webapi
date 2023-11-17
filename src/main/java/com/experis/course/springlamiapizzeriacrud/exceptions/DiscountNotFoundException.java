package com.experis.course.springlamiapizzeriacrud.exceptions;

import com.experis.course.springlamiapizzeriacrud.model.Discount;

public class DiscountNotFoundException extends RuntimeException{
    public DiscountNotFoundException(String message) {
        super(message);
    }
}
