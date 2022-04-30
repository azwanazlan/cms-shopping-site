package com.azwanazlan.cmsshoppingcart.models;

import java.util.List;

import com.azwanazlan.cmsshoppingcart.models.data.Page;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * PageRepository
 */
public interface PageRepository extends JpaRepository<Page, Integer>{

   @Override
   List<Page> findAll();

    
}  