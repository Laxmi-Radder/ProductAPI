package com.matchesfashion.papi.repository;

import com.matchesfashion.papi.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public List<Product> findByPriceGreaterThan(int price);

}
