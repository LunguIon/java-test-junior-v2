/*
 * Copyright (c) 2013-2022 Global Database Ltd, All rights reserved.
 */

package com.java.test.junior.service;

import com.java.test.junior.model.Product;
import com.java.test.junior.model.ProductDTO;
import com.java.test.junior.repository.ProductRepository;
import com.java.test.junior.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author dumitru.beselea
 * @version java-test-junior
 * @apiNote 08.12.2022
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    /**
     * @param productDTO this product to be created
     * @return the product created from the database
     */
    private UserRepository userRepository;
    private ProductRepository productRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setUser(userRepository.findByUsername(getCurrentUsername()));
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> editProduct(Long id, ProductDTO productDTO) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setUpdatedAt(LocalDateTime.now());
            return Optional.of(productRepository.save(product));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public String getCurrentUsername() {
        return "";
    }
}