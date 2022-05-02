package com.azwanazlan.cmsshoppingcart.models.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="categories")
@Data
public class Category {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @(min=2, message = "Name must be at least 2 characters long")
    private String name;

    private String slug;
    
    private int sorting;

}
