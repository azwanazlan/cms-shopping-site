package com.azwanazlan.cmsshoppingcart.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //identify this class as a component scanning
public class HomeController {
    
    @GetMapping("/someRandomPage")
    public String home() {
        return "home";
    }
}
