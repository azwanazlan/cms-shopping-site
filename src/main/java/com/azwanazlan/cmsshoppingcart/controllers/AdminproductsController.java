package com.azwanazlan.cmsshoppingcart.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.validation.Valid;

import com.azwanazlan.cmsshoppingcart.models.CategoryRepository;
import com.azwanazlan.cmsshoppingcart.models.ProductRepository;
import com.azwanazlan.cmsshoppingcart.models.data.Category;
import com.azwanazlan.cmsshoppingcart.models.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/admin/products")
public class AdminproductsController {
    
    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model) {

        List<Product> products = productRepo.findAll();
        model.addAttribute("products", products);
        return "admin/products/index";

    }
    @GetMapping("/add")
    public String add(@ModelAttribute Product product, Model model) { //Model model is because of category_id   //Product product is for th:object${product}

        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        
        return "admin/products/add";
    
    }

    @PostMapping("/add")
    public String add(@Valid Product product, 
                        BindingResult bindingResult, 
                        MultipartFile file, 
                        RedirectAttributes redirectAttributes, 
                        Model model) throws IOException { //file is for th:name="file"

        if (bindingResult.hasErrors()) {
            return "admin/products/add";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if (filename.endsWith("jpg") || filename.endsWith("png") ) {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "Product added");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");
        

        String slug = product.getName().toLowerCase().replace(" ", "-");
        
        Product productExists = productRepo.findBySlug(slug);

        if (! fileOK ){
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or png");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
        }

        if ( productExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            
        } else  { 
            product.setSlug(slug);
            product.setImage(filename);
            productRepo.save(product);
            Files.write(path, bytes);
        } 

        return "redirect:/admin/products/add";
    }

}

