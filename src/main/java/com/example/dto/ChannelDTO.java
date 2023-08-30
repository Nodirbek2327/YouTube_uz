package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO {
    private String id;
    @NotBlank
    private String name;
    private String imageId;
    private String description;
    private String bannerId;
    private Integer profileId;
    private LocalDateTime createdDate;
    private AttachDTO photo;
}
