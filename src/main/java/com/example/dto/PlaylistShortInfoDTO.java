package com.example.dto;

import com.example.mapper.VideoShortInfoMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistShortInfoDTO {
    private String playListId;
    private String playListName;
    private LocalDateTime createdDate;
    private ChannelDTO channel;
    private Integer videoCount;
    private List<VideoShortInfoMapper> videoList;
}
