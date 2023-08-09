package com.example.productshop.services.impl;

import com.example.productshop.models.dto.ProductSeedDto;
import com.example.productshop.models.entities.Product;
import com.example.productshop.models.entities.User;
import com.example.productshop.repositories.ProductRepository;
import com.example.productshop.services.CategoryService;
import com.example.productshop.services.ProductService;
import com.example.productshop.services.UserService;
import com.example.productshop.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static com.example.productshop.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {

    public static final String PRODUCTS_FILE_NAME = "products.json";
    private final ProductRepository productRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedProducts() throws IOException {

        if (productRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCE_FILE_PATH + PRODUCTS_FILE_NAME));

        ProductSeedDto[] productSeedDtos = gson.fromJson(fileContent, ProductSeedDto[].class);

        Arrays.stream(productSeedDtos).filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setSeller(userService.findRandomUser());
                    if(product.getPrice().compareTo(BigDecimal.valueOf(500L)) > 0){
                        User buyer = userService.findRandomUser();
                        if(product.getSeller() == buyer){
                            buyer = userService.findRandomUser();
                        } else {
                            product.setBuyer(buyer);
                        }
                    }
                    product.setCategories(categoryService.findRandomCategories());

                    return product;
                })
                .forEach(productRepository::save);
    }
}
