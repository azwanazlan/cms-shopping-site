package com.azwanazlan.cmsshoppingcart.models;



import com.azwanazlan.cmsshoppingcart.models.data.Page;

import org.springframework.data.jpa.repository.JpaRepository;



/**
 * PageRepository
 */
public interface PageRepository extends JpaRepository<Page, Integer>{

        Page findBySlug(String slug);
        
       // @Query("Select p FROM Page p Where p.id != :id and p.slug = :slug")
        //Page findBySlug(int id, String slug);

        Page findBySlugAndIdNot(String slug, int id);
}       
