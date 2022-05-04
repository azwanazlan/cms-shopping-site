package com.azwanazlan.cmsshoppingcart.models;

import com.azwanazlan.cmsshoppingcart.models.data.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    
}
