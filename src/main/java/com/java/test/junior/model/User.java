package com.java.test.junior.model;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    @OneToMany(mappedBy = "user")
    private Set<Product> products;
    @OneToMany(mappedBy = "user")
    private Set<Like> likes;
    @OneToMany(mappedBy = "user")
    private Set<Dislike> dislikes;

}
