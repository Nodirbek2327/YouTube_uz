package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    @Email(message = "invalid email format")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 5,message = "length should be greater than 5")
    private String password;
}
