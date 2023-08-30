package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDTO {
    private String message;
    private Boolean isError;
    private Object data;

    public ApiResponseDTO(String message, Boolean isError) {
        this.message = message;
        this.isError = isError;
    }

    public ApiResponseDTO(Boolean isError, Object data) {
        this.isError = isError;
        this.data = data;
    }
}
