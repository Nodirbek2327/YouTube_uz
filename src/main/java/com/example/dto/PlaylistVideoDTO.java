package com.example.dto;

import com.example.entity.PlayListEntity;
import com.example.entity.VideoEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoDTO extends BaseStringDTO {
    @NotBlank(message = "playlistId is required")
    private String playlistId;
    @NotBlank(message = "message is required")
    private String videoId;
    @NotNull(message = "orderNumber is required")
    private Integer orderNumber;
}
