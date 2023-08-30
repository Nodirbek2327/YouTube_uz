package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CategoryDTO {
    private Integer id;
    @NotNull(message = "Name required")
    @NotBlank(message = " Field cannot be empty")
    private String name;
    private LocalDateTime createdDate;
}
