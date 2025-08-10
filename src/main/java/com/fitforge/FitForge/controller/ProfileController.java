package com.fitforge.FitForge.controller;

import com.fitforge.FitForge.dto.ChangePasswordDto;
import com.fitforge.FitForge.dto.UserProfileDto;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElse(null);

        UserProfileDto profileDto = new UserProfileDto();
        if (user != null) {
            profileDto.setAge(user.getAge());
            profileDto.setHeightInCm(user.getHeightInCm());
            profileDto.setGender(user.getGender());
        }

        model.addAttribute("user", user);
        model.addAttribute("profileDto", profileDto);
        model.addAttribute("passwordDto", new ChangePasswordDto());

        return "profile";
    }

    @PostMapping("/profile/upload-picture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            userService.updateProfilePicture(username, file);
            redirectAttributes.addFlashAttribute("successMessage", "Profile picture updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload picture: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/update")
    public String updateUserProfile(@Valid @ModelAttribute("profileDto") UserProfileDto profileDto,
                                    BindingResult bindingResult,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "redirect:/profile?error=true";
        }

        String username = authentication.getName();
        userService.updateUserProfile(username, profileDto);

        redirectAttributes.addFlashAttribute("successMessage", "Profile details updated successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordDto") ChangePasswordDto passwordDto,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordError", "All password fields are required.");
            return "redirect:/profile";
        }

        try {
            String username = authentication.getName();
            userService.changePassword(username, passwordDto);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("passwordError", e.getMessage());
        }

        return "redirect:/profile";
    }
}