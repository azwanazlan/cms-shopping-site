package com.azwanazlan.cmsshoppingcart.models;
import com.azwanazlan.cmsshoppingcart.models.data.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
}
