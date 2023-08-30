package com.example.mapper;

import com.example.dto.ChannelDTO;
import com.example.dto.VideoDTO;
import com.example.entity.ChannelEntity;
import com.example.entity.VideoEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlaylistVideoMapper {
    private String playlistId;
    private VideoDTO video;
    private ChannelDTO channel;
    private LocalDateTime createdDate;
    private Integer orderNum;
}
