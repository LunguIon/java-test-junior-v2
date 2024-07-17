package com.java.test.junior.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
