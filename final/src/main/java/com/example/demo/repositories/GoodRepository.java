package com.example.demo.repositories;

import com.example.demo.models.Good;
import org.springframework.data.repository.CrudRepository;


public interface GoodRepository extends CrudRepository<Good, Long> {
    Good findByTitle(String title);
    Iterable<Good> findGoodsByCategoryId(long categoryId);
}
