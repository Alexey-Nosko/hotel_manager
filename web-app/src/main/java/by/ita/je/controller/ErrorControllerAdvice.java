package by.ita.je.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleClientErrors(HttpClientErrorException ex, Model model) {
        model.addAttribute("errorMessage", "Client error occurred: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public String handleServerErrors(HttpServerErrorException ex, Model model) {
        model.addAttribute("errorMessage", "Server error occurred: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralErrors(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error";
    }
}
