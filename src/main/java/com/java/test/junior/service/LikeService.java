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
    private final CurrentUserService currentUserService;
    public boolean likeProduct(Long productId) {
        User user = userRepository.findByUsername(currentUserService.getCurrentUsername());
        Product product = productRepository.findById(productId).orElse(null);

        if (likeRepository.existsByUserAndProduct(user, product)) {
            return false;
        }
        Like like = new Like();
        like.setUser(user);
        like.setProduct(product);
        likeRepository.save(like);
        dislikeRepository.deleteByUserAndProduct(user, product);
        return true;
    }

    public boolean dislikeProduct(Long productId) {
        User user = userRepository.findByUsername(currentUserService.getCurrentUsername());
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

}
