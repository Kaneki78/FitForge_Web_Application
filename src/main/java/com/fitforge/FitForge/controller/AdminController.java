package com.fitforge.FitForge.controller;

import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showAdminLoginPage() {
        return "admin-login";
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    @PostMapping("/users/promote/{id}")
    public String promoteUserToAdmin(@PathVariable("id") Long userId) {
        userService.promoteToAdmin(userId);
        return "redirect:/admin/dashboard";
    }
}