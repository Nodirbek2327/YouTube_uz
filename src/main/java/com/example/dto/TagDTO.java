package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TagDTO {
    private Integer id;
    @NotBlank(message = " Field cannot be empty")
    private String name;
    private LocalDateTime createdDate;

    public TagDTO(Integer id, String name) {
        this.id=id;
        this.name=name;
    }
}
