package com.example.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoShortInfoMapper {
    private String videoId;
    private String name;
    private Long duration;

    public VideoShortInfoMapper(String videoId, String name, Long duration) {
        this.videoId = videoId;
        this.name = name;
        this.duration = duration;
    }
}
