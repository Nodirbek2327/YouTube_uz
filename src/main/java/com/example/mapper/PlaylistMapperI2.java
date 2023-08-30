package com.example.mapper;

import java.time.LocalDateTime;

public interface PlaylistMapperI2 {
     String getPlaylistId();
     String getPlaylistName();
     Integer getVideoCount();
     Integer getTotalViewCount();
     LocalDateTime getLastUpdateDate();
}
