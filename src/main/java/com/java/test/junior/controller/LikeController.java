package com.java.test.junior.controller;

import com.java.test.junior.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/products")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeProduct(@PathVariable Long id) {
        boolean liked = likeService.likeProduct(id);
        if (liked) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikeProduct(@PathVariable Long id) {
        boolean disliked = likeService.dislikeProduct(id);
        if (disliked) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
