package com.example.demo.controllers;

import com.example.demo.models.Good;
import com.example.demo.dto.Quant;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.GoodRepository;
import com.example.demo.service.TokenServiseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GoodController {
    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TokenServiseImpl tokenServise;
    /**
     * Создание нового товара
     * */
    @PostMapping(value = "/good/new")
    public ResponseEntity<String> addNewGood(@RequestBody Good good){
        if(categoryRepository.findByCategoryName(good.getCategory().getCategoryName()) == null){
            return  new ResponseEntity<>("Такой категории товаров нет", HttpStatus.BAD_REQUEST);
        }
        if(goodRepository.findByTitle(good.getTitle()) == null){
            //установка категории(сл. строка) похожа на костыль, но без нее выдоет ошибку
            good.setCategory(categoryRepository.findByCategoryName(good.getCategory().getCategoryName()));
            goodRepository.save(good);
            return new ResponseEntity<>("Товар добавлен в БД: " + good.getTitle(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Такой товар уже существует!", HttpStatus.BAD_REQUEST);
    }

    /**  1 ВАРИАНТ
     * GET Добавление заданного количестка к-л товара
     * */
    @GetMapping("/good/add={count}&id={id}")
    public ResponseEntity<String> addQQuantityGET(@PathVariable("id") long id,
                                               @PathVariable("count") int count){
        Good good = goodRepository.findById(id).orElse(null);
        if(good != null){
            good.setQuantityInStorage(good.getQuantityInStorage()+count);
            goodRepository.save(good);
            return new ResponseEntity<>("Количество товара изменено. На складе: " + good.getQuantityInStorage(),HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Товар не найден!", HttpStatus.NOT_FOUND);
    }
    /**  2 ВАРИАНТ. в объекте Quant только id и количество, которые нужно добавить
     * PUT Добавление заданного количестка к-л товара
     * А вообще, можно передавать только целое тело и его сохранять.
     * Но тогда у клиента должен быть весь объект. см.Вар 3
     * */
    @PutMapping("/good/addQuant")
    public ResponseEntity<String> addQuantPUT(@RequestBody Quant quant){
        Good good = goodRepository.findById(quant.getId()).orElse(null);
        System.out.println(good);
        if(good != null && quant.getCount() > 0){
            good.setQuantityInStorage(good.getQuantityInStorage() + quant.getCount());
            goodRepository.save(good);
            return new ResponseEntity<>("Количество товара изменено. На складе: " + good.getQuantityInStorage(),HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Товар не найден!", HttpStatus.NOT_FOUND);
    }
    /**
     *   3 ВАРИАНТ
     *   PUT Добавление заданного количестка к-л товара. Да и цену тутже менять можно, и все что угодно в объекте
     */
    @PutMapping("/good/addQuantity")
    public ResponseEntity<String> addQuantityPUT(@RequestBody Good good){
        if(goodRepository.findById(good.getId()).isPresent()){
            System.out.println(good);
            goodRepository.save(good);
            return new ResponseEntity<>("Количество товара изменено. На складе: " + good.getQuantityInStorage(),HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Товар не найден!", HttpStatus.NOT_FOUND);
    }

    /**
     * Вывод всех товаров
     */
    @GetMapping("/good/")
    public ResponseEntity<?> getGoods(){
        Iterable<Good> goods = goodRepository.findAll();
        return goods != null
                ? new ResponseEntity<>(goods, HttpStatus.OK)
                : new ResponseEntity<>("Товаров не найдено", HttpStatus.NOT_FOUND);
    }


    /**
     * Вывод данной категории товаров
     * */
    @GetMapping("/good/category={id}")
    public ResponseEntity<?> getGoodsByCategory(@PathVariable("id") long id){
        Iterable<Good>  goods = goodRepository.findGoodsByCategoryId(id);
        return goods != null
                ? new ResponseEntity<>(goods, HttpStatus.OK)
                : new ResponseEntity<>("Товаров не найдено", HttpStatus.NOT_FOUND);
    }

    /**
     * Отгрузка товара
     * */
    @PutMapping("/good/delQuant")
    public ResponseEntity<String> delQuantPUT(@RequestBody Quant quant,
                                              @CookieValue(value = "tokenByMe", required = false) String token){
        if(tokenServise.findByTokenValue(token) != null){                                   // проверка токена в БД
            Good good = goodRepository.findById(quant.getId()).orElse(null);           // проверка товара в БД
            if(good != null && quant.getCount() < good.getQuantityInStorage() && good.getQuantityInStorage() > 0){
                good.setQuantityInStorage(good.getQuantityInStorage() - quant.getCount());
                goodRepository.save(good);
                return new ResponseEntity<>("Количество товара изменено. На складе: " + good.getQuantityInStorage(),HttpStatus.FOUND);
            }
            return new ResponseEntity<>("Товар не найден, либо его недостаточно на складе!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Вы не авторизованы!",HttpStatus.FORBIDDEN);
    }


}
