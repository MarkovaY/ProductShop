package com.example.productshop.services.impl;

import com.example.productshop.models.dto.CategorySeedDto;
import com.example.productshop.models.entities.Category;
import com.example.productshop.repositories.CategoryRepository;
import com.example.productshop.services.CategoryService;
import com.example.productshop.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.productshop.constants.GlobalConstants.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORIES_FILE_NAME = "categories.json";
    private final CategoryRepository categoryRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedCategories() throws IOException {

        if(categoryRepository.count() > 0){
            return;
        }
        String fileContent = Files.readString(Path.of(RESOURCE_FILE_PATH + CATEGORIES_FILE_NAME));

        CategorySeedDto[] categorySeedDTOs = gson.fromJson(fileContent, CategorySeedDto[].class);

        Arrays.stream(categorySeedDTOs).filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);
    }

    @Override
    public Set<Category> findRandomCategories() {

        Set<Category> categorySet = new HashSet<>();
        int categoryCount = ThreadLocalRandom.current().nextInt(1, 3);
        long totalCategoriesCount = categoryRepository.count();

        for (int i = 0; i < categoryCount; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, totalCategoriesCount + 1);

            categorySet.add(categoryRepository.findById(randomId).orElse(null));
        }
        return categorySet;
    }
}
