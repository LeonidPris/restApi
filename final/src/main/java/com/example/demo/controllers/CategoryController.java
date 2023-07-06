package com.example.demo.controllers;

import com.example.demo.models.GoodCategory;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    /**
     * Создание новой категории
     * */
    @PostMapping(value = "/category/new")
    public ResponseEntity<String> addNewCategory(@RequestBody GoodCategory category){
        if(categoryRepository.findByCategoryName(category.getCategoryName()) == null){
            categoryRepository.save(category);
            return new ResponseEntity<>("Новая категория товаров создана: " + category.getCategoryName(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Категория уже существует!", HttpStatus.BAD_REQUEST);
        }
    }
}
