package com.example.demo.controllers;

import com.example.demo.models.Token;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.TokenServiseImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenServiseImpl tokenServise;

    /**
     * Создание юзера + Auth
     * */

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestBody User user,
                                        HttpServletResponse response){
        user.setToken(tokenServise.createToken(user));
        userRepository.save(user);
        Cookie cookie = new Cookie("tokenByMe", user.getToken().getTokenValue());
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        response.setContentType("text/plain");
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    /**
     * Вернуть всех юзеров
     * */
    @GetMapping("/user/get")
    public ResponseEntity<?> getUsers(){
        try {
            return new ResponseEntity<>(userRepository.findAll(), HttpStatus.FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
