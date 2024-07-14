package com.java.test.junior.service;

import com.java.test.junior.model.Dislike;
import com.java.test.junior.model.Like;
import com.java.test.junior.model.Product;
import com.java.test.junior.model.User;
import com.java.test.junior.repository.DislikeRepository;
import com.java.test.junior.repository.LikeRepository;
import com.java.test.junior.repository.ProductRepository;
import com.java.test.junior.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final DislikeRepository dislikeRepository;
    public boolean likeProduct(Long productId) {
        String currentUsername = getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername);
        Product product = productRepository.findById(productId).get();

        if (likeRepository.existsByUserAndProduct(user, product)) {
            return false;
        }
        Like like = new Like();
        like.setUser(user);
        like.setProduct(product);
        dislikeRepository.deleteByUserAndProduct(user, product);
        return true;
    }

    public boolean dislikeProduct(Long productId) {
        String currentUsername = getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername);
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null || dislikeRepository.existsByUserAndProduct(user, product)) {
            return false;
        }

        Dislike dislike = new Dislike();
        dislike.setUser(user);
        dislike.setProduct(product);
        dislikeRepository.save(dislike);

        likeRepository.deleteByUserAndProduct(user, product);

        return true;
    }



    private String getCurrentUsername() {
        // This method should return the currently authenticated user's username
        return "ehehehe";
    }
}
