package com.java.test.junior.repository;

import com.java.test.junior.model.Dislike;
import com.java.test.junior.model.Product;
import com.java.test.junior.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DislikeRepository extends JpaRepository<Dislike, Integer> {
    boolean existsByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
}
