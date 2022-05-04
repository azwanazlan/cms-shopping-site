package com.azwanazlan.cmsshoppingcart.controllers;

import java.util.List;

import com.azwanazlan.cmsshoppingcart.models.ProductRepository;
import com.azwanazlan.cmsshoppingcart.models.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/admin/products")
public class AdminproductsController {
    
    @Autowired
    private ProductRepository productRepo;

    @GetMapping
    public String index(Model model) {

        List<Product> products = productRepo.findAll();
        model.addAttribute("products", products);

        return "admin/products/index";

    }
}

