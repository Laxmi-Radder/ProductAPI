package com.matchesfashion.papi.rest;

import com.matchesfashion.papi.domain.Product;
import com.matchesfashion.papi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Product controller deals with all product related APIs
 */
@RestController
@RequestMapping("/v1")
public class ProductController {

    /**
     * Database Repository.
     */
    @Autowired
    ProductRepository productRepository;

    /**
     * returns all the products in database
     * @return all products list
     */
    @GetMapping(value = "/all-products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    /**
     * returns products whose price is greater than input price
     * @param price
     * @return products list
     */
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProductsPriceMoreThan(@RequestParam(required = true) int price) {
        return ResponseEntity.ok(productRepository.findByPriceGreaterThan(price));
    }

}
