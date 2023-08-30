package com.example.mapper;

import java.time.LocalDateTime;

public interface PlaylistShortInfoMapperI {
    String getPlaylistId();
    String getPlaylistName();
    LocalDateTime getPlaylistCreatedDate();
    String getChannelId();
    String getChannelName();

}
