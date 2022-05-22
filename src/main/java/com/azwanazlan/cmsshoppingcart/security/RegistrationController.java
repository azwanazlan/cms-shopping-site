package com.azwanazlan.cmsshoppingcart.security;

import javax.validation.Valid;

import com.azwanazlan.cmsshoppingcart.models.UserRepository;
import com.azwanazlan.cmsshoppingcart.models.data.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String register(User user) {  //user is from th:object in register.html
        return "register";
    }

    @PostMapping
    public String register(@Valid User user, BindingResult bindingResult, Model model) {  //user is from th:object in register.html
        
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (! user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordMatchProblem", "Passwords do not match!");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return "redirect:/login";
    }
}
