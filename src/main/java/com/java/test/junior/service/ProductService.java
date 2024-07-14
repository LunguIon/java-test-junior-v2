/*
 * Copyright (c) 2013-2022 Global Database Ltd, All rights reserved.
 */

package com.java.test.junior.service;

import com.java.test.junior.model.Product;
import com.java.test.junior.model.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * @author dumitru.beselea
 * @version java-test-junior
 * @apiNote 08.12.2022
 */
public interface ProductService {
    /**
     * @param productDTO this product to be created
     * @return the product created from the database
     */
    Product createProduct(ProductDTO productDTO);
    Optional<Product> getProduct(Long id);
    Optional<Product> editProduct(Long id, ProductDTO productDTO);
    boolean deleteProduct(Long id);
    Page<Product> getAllProducts(Pageable pageable);
    String getCurrentUsername();

}