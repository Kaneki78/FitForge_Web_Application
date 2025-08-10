package com.fitforge.FitForge.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMsg = "An unexpected error occurred.";
        String errorCode = "Error";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            errorCode = statusCode.toString();

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMsg = "Oops! The page you are looking for does not exist.";
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMsg = "We're sorry, but something went wrong on our end. Please try again later.";
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMsg = "You do not have permission to access this page.";
            }
        }

        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMsg);
        return "error"; // This will look for an error.html template
    }
}