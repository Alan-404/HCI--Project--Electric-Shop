package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    
}
