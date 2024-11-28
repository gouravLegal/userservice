package com.example.userservice.services;

import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repos.TokenRepo;
import com.example.userservice.repos.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService {

    private UserRepo userRepo;
    private TokenRepo tokenRepo;

    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, TokenRepo tokenRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(String name, String email, String password) {
        //TODO validation to check if user already exists

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(passwordEncoder.encode(password));

        return userRepo.save(user);
    }

    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);

        if(optionalUser.isEmpty())
            throw new UsernameNotFoundException("User with Email id " + email + " not found");

        User user = optionalUser.get();
        if(!passwordEncoder.matches(password, user.getHashedPassword()))
            throw new BadCredentialsException("Wrong password");

        Token token = generateToken(user);
        return tokenRepo.save(token);
    }

    private Token generateToken(User user) {
        Token token = new Token();
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setExpiry(System.currentTimeMillis() + 1000 * 60 * 60 * 24); //Setting expiry as 24 hours
        token.setUser(user);
        return token;
    }

    public User validateToken(String token) {

        //TODO We have not implemented a self-validating token here and so we need to run a DB query to validate the token.
        // Correct implementation is to use a checksum (generated using a private key) in the token and validate if the checksum is correct once we get the token back in request
        /*
            A Token is valid if:
            1. Token exists in DB
            2. Token has not expired
            3. Token is not marked as deleted
         */

        Optional<Token> optionalToken = tokenRepo.findByValueAndDeletedIsAndExpiryGreaterThan(token, false, System.currentTimeMillis());

        if (optionalToken.isEmpty())
            return null;
        else
            return optionalToken.get().getUser();
    }
}
