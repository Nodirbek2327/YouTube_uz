package com.example.mapper;

import com.example.dto.ChannelDTO;
import com.example.dto.ProfileDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public interface PlaylistMapperI {
     String getPlaylistId();
     String getPlaylistName();
     String getPlaylistDescription();
     AccessLevel getPlaylistStatus();
     Integer getPlaylistOrderNum();
     String getChannelId();
     String getChannelName();
     String getChannelPhotoId();
     Integer getProfileId();
     String getProfileName();
     String getProfileSurname();
     String getProfilePhotoId();


}
