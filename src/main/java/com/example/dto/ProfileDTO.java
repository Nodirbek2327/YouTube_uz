package com.example.dto;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;

    @NotBlank(message = "Field must have some value")
    private String name;

    @NotBlank(message = "Field must have some value")
    private String surname;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Field must have some value")
    private String password;
    private ProfileRole role;
    private String imageId;
    private LocalDateTime createDate;
    private ProfileStatus status;
    private String jwt;
    private String mainPhotoUrl;
    private AttachDTO attachDTO;

    public ProfileDTO() {
    }

    public ProfileDTO(Integer id, String name, String surname, String email, String mainPhotoUrl) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email=email;
        this.mainPhotoUrl=mainPhotoUrl;
    }

    public ProfileDTO(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public ProfileDTO(Integer id, String email) {
        this.id = id;
        this.email=email;
    }

    public ProfileDTO(Integer id, String name, String surname, AttachDTO attachDTO) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.attachDTO = attachDTO;
    }
}
