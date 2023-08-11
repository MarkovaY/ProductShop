package com.example.productshop.services.impl;

import com.example.productshop.models.dto.UserSeedDto;
import com.example.productshop.models.entities.User;
import com.example.productshop.repositories.UserRepository;
import com.example.productshop.services.UserService;
import com.example.productshop.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.productshop.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_FILE_NAME = "users.json";
    private final UserRepository userRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public UserServiceImpl(UserRepository userRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedUsers() throws IOException {

        if(userRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCE_FILE_PATH + USERS_FILE_NAME));

        Arrays.stream(gson.fromJson(fileContent, UserSeedDto[].class))
                .filter(validationUtil::isValid)
                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                .forEach(userRepository::save);
    }

    @Override
    public User findRandomUser() {

        long randomId = ThreadLocalRandom.current().nextLong(1, userRepository.count() + 1);
        return userRepository.findById(randomId).orElse(null);
    }

    @Override
    public void findAllUsersWithAtLeastOneSoldProduct() {
        userRepository.findAllUsersWithAtLeastOneProductSoldOrderByLastNameThenByFirstName();
    }
}
