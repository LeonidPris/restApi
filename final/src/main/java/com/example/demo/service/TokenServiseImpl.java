package com.example.demo.service;

import com.example.demo.models.Token;
import com.example.demo.models.User;
import com.example.demo.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiseImpl implements TokenServise{
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Token createToken(User user){
        Token token = new Token("123"+user.getPassword()); // +123-здесь могла быть моя зашифрованная строка.
        tokenRepository.save(token);
        return token;
    }
    @Override
    public Token findByTokenValue(String value){
        return tokenRepository.findByTokenValue(value);
    }


}
