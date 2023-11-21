package com.experis.course.springlamiapizzeriacrud.exceptions;

public class PizzaNameUniqueException extends RuntimeException {
    public  PizzaNameUniqueException(String message) {
        super(message);
    }
}
