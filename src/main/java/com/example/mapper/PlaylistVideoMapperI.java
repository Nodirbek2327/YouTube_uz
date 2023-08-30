package com.example.mapper;

import com.example.entity.ChannelEntity;
import com.example.entity.VideoEntity;

import java.time.LocalDateTime;

public interface PlaylistVideoMapperI {
     String getPlaylistId();
     String getVideoId();
     String getPreviewAttachId();
     String getVideoTitle();
     Long getAttachDuration();
     String getChannelId();
     String getChannelName();
     LocalDateTime getPVCreatedDate();
     Integer getPVOrderNum();
}
