/*
 * Copyright (c) 2013-2022 Global Database Ltd, All rights reserved.
 */

package com.java.test.junior.service;

import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.Product;
import com.java.test.junior.model.ProductDTO;
import com.java.test.junior.model.User;
import com.java.test.junior.repository.ProductRepository;
import com.java.test.junior.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;
    private final ProductMapper productMapper;

    private static final Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());
    private static final String ADMIN = "admin";
    @Override
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setUser(userRepository.findByUsername(currentUserService.getCurrentUsername()));
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
    public List<Product> searchProductsByName(String name) {
        return productMapper.searchProductsByName("%" + name + "%");
    }

    @Override
    public boolean loadProducts(String fileAddress) {
        try {
            List<Product> products = fetchAndParseFile(fileAddress);

            User admin = ensureAdminUserExists();

            for (Product product : products) {
                product.setUser(admin);
                productRepository.save(product);
            }
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading products", e);
            return false;
        }
    }

    private List<Product> fetchAndParseFile(String fileAddress) throws Exception {
        List<Product> products = new ArrayList<>();

        if (fileAddress.startsWith("http")) {
            URL url = new URL(fileAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            String auth = "admin:admin";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            String authHeader = "Basic " + encodedAuth;
            connection.setRequestProperty("Authorization", authHeader);

            int responseCode = connection.getResponseCode();
            if (responseCode == 401) {
                throw new IOException("Unauthorized access to URL: " + fileAddress);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields.length >= 3) {
                        Product product = new Product();
                        product.setName(fields[0]);
                        product.setPrice(Double.parseDouble(fields[1]));
                        product.setDescription(fields[2]);
                        products.add(product);
                    } else {
                        logger.log(Level.WARNING, "Invalid line: {0}", line);
                    }
                }
            }
        } else {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileAddress);
            if (inputStream == null) {
                inputStream = Files.newInputStream(Paths.get(fileAddress));
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                boolean isHeader = true;
                while ((line = reader.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }
                    String[] fields = line.split(",");
                    if (fields.length >= 3) {
                        Product product = new Product();
                        product.setName(fields[0]);
                        product.setPrice(Double.parseDouble(fields[1]));
                        product.setDescription(fields[2]);
                        products.add(product);
                    } else {
                        logger.log(Level.WARNING, "Invalid line: {0}", line);
                    }
                }
            }
        }

        return products;
    }


    private User ensureAdminUserExists() {
        User admin = userRepository.findByUsername(ADMIN);
        if (admin == null) {
            admin = new User();
            admin.setUsername(ADMIN);
            admin.setPassword(ADMIN);
            admin = userRepository.save(admin);
        }
        return admin;
    }



}