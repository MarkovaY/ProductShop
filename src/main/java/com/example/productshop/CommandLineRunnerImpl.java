package com.example.productshop;

import com.example.productshop.models.dto.ProductNameAndPriceDto;
import com.example.productshop.models.entities.User;
import com.example.productshop.services.CategoryService;
import com.example.productshop.services.ProductService;
import com.example.productshop.services.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

//    A products shop holds users, products and categories for the products. Users can sell and buy products.

    public static final String OUTPUT_FILES_PATH = "src/main/resources/files/out/";
    private static final String PRODUCTS_IN_RANGE_FILE_NAME = "products-in-range.json";
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;
    private final Gson gson;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

//    Import the data from the provided files (users.json, products.json, categories.json).
//    Import the users first. When importing the products, randomly select the buyer and seller from the existing users.
//    Leave out some products that have not been sold (i.e. buyer is null).
//    Randomly generate categories for each product from the existing categories.

    @Override
    public void run(String... args) throws Exception {

        seedData();

        System.out.println("Enter exercise:");
        int exerciseNumber = Integer.parseInt(bufferedReader.readLine());

        switch(exerciseNumber) {
            case 1 -> productsInRange();
            case 2 -> successfullySoldProducts();
        }
    }

    private void successfullySoldProducts() {

        userService.findAllUsersWithAtLeastOneSoldProduct();
    }

    private void productsInRange() throws IOException {
//    Get all products in a specified price range (e.g. 500 to 1000), which have no buyer.
//    Order them by price (from lowest to highest).
//    Select only the product name, price and the full name of the seller.
//    Export the result to JSON.
        List<ProductNameAndPriceDto> productDtos = productService.findAllProductsInRangeOrderedByPrice(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));

        String content = gson.toJson(productDtos);

        writeToFile(OUTPUT_FILES_PATH + PRODUCTS_IN_RANGE_FILE_NAME, content);
    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProducts();
    }
}
