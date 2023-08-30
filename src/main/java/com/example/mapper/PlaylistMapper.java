package com.example.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistMapper {
    private String id;
    private String name;
    private Integer videoCount;
    private Integer totalViewCount;
    private LocalDateTime lastUpdateDate;

    public PlaylistMapper(String id, String name, Integer videoCount, Integer totalViewCount, LocalDateTime lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.videoCount = videoCount;
        this.totalViewCount = totalViewCount;
        this.lastUpdateDate = lastUpdateDate;
    }
}
