package com.fitforge.FitForge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Tells Spring this is a Controller class
public class WelcomeController {

    // This method listens for web requests to the homepage ("/")
    @GetMapping("/")
    public String showWelcomePage() {
        // This tells Spring to find and show the HTML file named "index.html"
        return "index";
    }
}