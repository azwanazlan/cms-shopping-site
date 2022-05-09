package com.azwanazlan.cmsshoppingcart.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import com.azwanazlan.cmsshoppingcart.models.CategoryRepository;
import com.azwanazlan.cmsshoppingcart.models.ProductRepository;
import com.azwanazlan.cmsshoppingcart.models.data.Category;
import com.azwanazlan.cmsshoppingcart.models.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String index(Model model, @RequestParam(value="page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 0 ;
        double pageCount;

        Pageable pagable = PageRequest.of(page, perPage);
        //List<Product> products = productRepo.findAll();
        Page<Product> products = productRepo.findAll(pagable);

        List<Category> categories = categoryRepo.findAll();
       
        HashMap<Integer, String> cats = new HashMap<>();
        for (Category cat : categories){
            cats.put(cat.getId(), cat.getName());
        }

        model.addAttribute("products", products);
        model.addAttribute("cats", cats);

        Long count = productRepo.count();
        pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);


        return "admin/products/index";

    }
    @GetMapping("/add")
    public String add(@ModelAttribute Product product, Model model) { //Model model is because of category_id   //Product product is for th:object${product}

    List<Category> categories = categoryRepo.findAll();
       
    model.addAttribute("categories", categories);
        
    return "admin/products/add";
    
    }

    @PostMapping("/add")
    public String add(@Valid Product product, BindingResult bindingResult, MultipartFile file, RedirectAttributes redirectAttributes, Model model ) throws IOException { //file is for th:name="file"
        
        List<Category> categories = categoryRepo.findAll();

        
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
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

        

        if ( ! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or png");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("product", product);

        }

        else if ( productExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
            
        } else  { 
            product.setSlug(slug);
            product.setImage(filename);
            productRepo.save(product);
            //String filename2 = product.getId() + "_" + filename;

            //Path path = Paths.get("src/main/resources/static/media/" + filename2);
            Files.write(path , bytes);
        } 

        return "redirect:/admin/products/add";
    }
    
    
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        
        
        Product product = productRepo.getById(id);

        List<Category> categories = categoryRepo.findAll();


        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "admin/products/edit";

    }

    @PostMapping("/edit")
    public String edit(@Valid Product product, 
                        BindingResult bindingResult, 
                        MultipartFile file, 
                        RedirectAttributes redirectAttributes, 
                        Model model ) throws IOException { //file is for th:name="file"
        
        Product currentProduct = productRepo.getById(product.getId());
        
        List<Category> categories = categoryRepo.findAll();
    
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
            model.addAttribute("productName", currentProduct.getName());

            return "admin/products/edit";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
     
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if(!file.isEmpty()) {
            if (filename.endsWith("jpg") || filename.endsWith("png") ) {
                fileOK = true;
            }

        } else {
            fileOK = true;
        }
        
       

        redirectAttributes.addFlashAttribute("message", "Product edited");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");
        

        String slug = product.getName().toLowerCase().replace(" ", "-");
        
        Product productExists = productRepo.findBySlugAndIdNot(slug, product.getId());

        

        if ( ! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or png");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("product", product);

        }

        else if ( productExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
            
        } else  { 
            product.setSlug(slug);

            if (!file.isEmpty()) {
                Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
                Files.delete(path2);
                product.setImage(filename);
                Files.write(path, bytes);

            } else {
            product.setImage(currentProduct.getImage());

            }
            productRepo.save(product);

        } 

        return "redirect:/admin/products/edit/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete( @PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {

        Product product = productRepo.getById(id);
        Product currentProduct = productRepo.getById(product.getId());


        productRepo.deleteById(id);
        Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
        Files.delete(path2);
        redirectAttributes.addFlashAttribute("message", "Product deleted");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");
      

        return "redirect:/admin/products";
    }

}

