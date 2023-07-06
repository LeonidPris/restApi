package com.example.demo.service;

import com.example.demo.models.Token;
import com.example.demo.models.User;

public interface TokenServise {
    /**
     * Метод создающий токен
     * */
    Token createToken(User user);

    /**
     * Поиск токена по значению
     */
    Token findByTokenValue(String value);
}
