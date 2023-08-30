package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponeDTO {
    private boolean status;
    private String message;
    private Object data;

    public ApiResponeDTO(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
    public ApiResponeDTO(boolean status, Object data) {
        this.status = status;
        this.data=data;
    }
}
