package com.fitforge.FitForge.service;

import com.fitforge.FitForge.dto.ChangePasswordDto;
import com.fitforge.FitForge.dto.UserProfileDto;
import com.fitforge.FitForge.dto.UserRegistrationDto;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Path rootLocation = Paths.get("user-uploads");

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    public void registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) { throw new IllegalStateException("Username already exists"); }
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) { throw new IllegalStateException("Email already exists"); }
        User user = new User();
        user.setFullName(registrationDto.getFullName());
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setRole(registrationDto.getRole());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userRepository.save(user);
    }

    public List<User> findAllUsers() { return userRepository.findAll(); }

    @Transactional
    public void promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    @Transactional
    public void updateUserProfile(String username, UserProfileDto profileDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        user.setAge(profileDto.getAge());
        user.setHeightInCm(profileDto.getHeightInCm());
        user.setGender(profileDto.getGender());
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(String username, ChangePasswordDto passwordDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword())) { throw new IllegalStateException("Invalid current password"); }
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmNewPassword())) { throw new IllegalStateException("New passwords do not match"); }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateProfilePicture(String username, MultipartFile file) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (file.isEmpty()) { throw new IllegalStateException("Cannot upload an empty file."); }
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(uniqueFilename));
            user.setProfilePictureFilename(uniqueFilename);
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}