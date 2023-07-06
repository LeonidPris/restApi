package com.example.demo.repositories;

import com.example.demo.models.GoodCategory;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<GoodCategory, Long> {
    GoodCategory findByCategoryName(String categoryName);
    boolean existsByCategoryName(String categoryName);
}
