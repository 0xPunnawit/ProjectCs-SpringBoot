package com.example.Tutorials_Eom.controller;

import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        String hashedPassword = PasswordEncoder.encodePassword(password);

        Optional<Users> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent() && userOptional.get().getPasswordHash().equals(hashedPassword)) {
            Users user = userOptional.get();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60);

            return "redirect:/home";
        }

        boolean usernameExists = userRepository.existsByUsername(username);

        if (!usernameExists) {
            model.addAttribute("error", "Username not found");
        } else {
            model.addAttribute("error", "Incorrect password");
        }

        model.addAttribute("username", username);
        model.addAttribute("password", password);

        return "login";
    }
}
