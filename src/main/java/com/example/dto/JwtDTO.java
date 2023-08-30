package com.example.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtDTO {

    private Integer id;
    private String email;

    public JwtDTO(Integer id, String email) {
        this.id = id;
        this.email = email;
    }
}
