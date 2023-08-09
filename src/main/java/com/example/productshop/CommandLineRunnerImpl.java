package com.example.productshop;

import com.example.productshop.services.CategoryService;
import com.example.productshop.services.ProductService;
import com.example.productshop.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

//    A products shop holds users, products and categories for the products. Users can sell and buy products.

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
    }

//    Import the data from the provided files (users.json, products.json, categories.json).
//    Import the users first. When importing the products, randomly select the buyer and seller from the existing users.
//    Leave out some products that have not been sold (i.e. buyer is null).
//    Randomly generate categories for each product from the existing categories.

    @Override
    public void run(String... args) throws Exception {

        seedData();
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProducts();
    }
}
