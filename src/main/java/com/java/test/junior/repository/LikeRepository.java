package com.java.test.junior.repository;

import com.java.test.junior.model.Like;
import com.java.test.junior.model.Product;
import com.java.test.junior.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
}
