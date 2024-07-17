package com.java.test.junior.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.model.Product;
import com.java.test.junior.model.ProductDTO;
import com.java.test.junior.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProductControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
     void setUp() {

        productService.createProduct(new ProductDTO("Test Product", 100.0, "this is a test product"));
    }

    @Test
     void testCreateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO("New Product", 200.0, "this is a new product");
        String requestBody = objectMapper.writeValueAsString(productDTO);

        ResponseEntity<Product> response = restTemplate.postForEntity("/products", productDTO, Product.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New Product", response.getBody().getName());
    }

    @Test
     void testGetProduct() throws Exception {
        ResponseEntity<Product> response = restTemplate.getForEntity("/products/1", Product.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Product", response.getBody().getName());
    }

    @Test
     void testEditProduct() throws Exception {
        ProductDTO updatedProductDTO = new ProductDTO("Updated Product", 150.0, "this is an updated product");
        String requestBody = objectMapper.writeValueAsString(updatedProductDTO);

        restTemplate.put("/products/1", updatedProductDTO);

        ResponseEntity<Product> response = restTemplate.getForEntity("/products/1", Product.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Product", response.getBody().getName());
    }

    @Test
     void testDeleteProduct() throws Exception {
        restTemplate.delete("/products/1");

        ResponseEntity<Product> response = restTemplate.getForEntity("/products/1", Product.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
     void testGetAllProducts() throws Exception {
        ResponseEntity<Page<Product>> response = restTemplate.exchange(
                "/products?page=1&page_size=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Page<Product>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(1, response.getBody().getTotalElements());
    }


    @Test
     void testSearchProductsByName() throws Exception {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/products/search?name=Test", Product[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
        assertEquals("Test Product", response.getBody()[0].getName());
    }

    @Test
     void testLoadProducts() throws Exception {
        String fileAddress = "path/to/file.csv";

        ResponseEntity<String> response = restTemplate.postForEntity("/products/loading/products?fileAddress=" + fileAddress, null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Products loaded successfully", response.getBody());
    }

    @Bean
     ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public PostgreSQLContainer<?> postgreSQLContainer() {
            return postgreSQLContainer;
        }
    }

}

