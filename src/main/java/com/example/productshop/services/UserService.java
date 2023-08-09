package com.example.productshop.services;

import com.example.productshop.models.entities.User;

import java.io.IOException;

public interface UserService {
    void seedUsers() throws IOException;

    User findRandomUser();
}
