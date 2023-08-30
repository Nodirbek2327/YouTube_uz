package com.example.dto;

import com.example.entity.ChannelEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayListDTO extends BaseStringDTO {
    @NotBlank(message = "channel id is required")
    private String channelId;
    @NotBlank(message = "playlist name is required")
    private String name;
    private String description;
    @NotNull(message = "status is required")
    private AccessLevel status;
    @NotNull(message = "status is required")
    private Integer orderNumber;
    private ChannelDTO channelDTO;
    private ProfileDTO profileDTO;

}
