package com.example.service;

import com.example.dto.*;
import com.example.entity.PlayListEntity;
import com.example.entity.PlaylistVideoEntity;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.exp.UnAuthorizedException;
import com.example.mapper.PlaylistVideoMapper;
import com.example.mapper.PlaylistVideoMapperI;
import com.example.repository.PlayListRepository;
import com.example.repository.PlaylistVideoRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistVideoService {
    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;
    @Autowired
    private PlayListRepository playListRepository;
    @Autowired
    private AttachService attachService;
// 1. Create (User and Owner)
    @Autowired
    private PlayListService playListService;

    // 1. Create (User and Owner)
    public ApiResponseDTO create(PlaylistVideoDTO dto) {
        Optional<PlaylistVideoEntity> optional=playlistVideoRepository
               .findByPlaylistIdAndVideoIdAndVisibleTrue(dto.getPlaylistId(), dto.getVideoId());
        if(optional.isPresent()) return new ApiResponseDTO("video add",false);
        PlaylistVideoEntity entity=new PlaylistVideoEntity();
        entity.setPlaylistId(dto.getPlaylistId());
        entity.setVideoId(dto.getVideoId());
        entity.setOrderNumber(dto.getOrderNumber());
        playlistVideoRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return new ApiResponseDTO(false,dto);
    }
//  2. Update
    public ApiResponseDTO update( PlaylistVideoDTO dto) {
        Integer profileId= SpringSecurityUtil.getProfileId();
        PlaylistVideoEntity entity=get(dto.getPlaylistId(), dto.getVideoId());
        Optional<PlayListEntity>playlist=playListRepository.findPlaylist(dto.getPlaylistId(), profileId);
        if(playlist.isEmpty()) throw new UnAuthorizedException("Playlist not belong to you!");
        entity.setVideoId(dto.getVideoId());
        entity.setPlaylistId(dto.getPlaylistId());
        entity.setOrderNumber(dto.getOrderNumber());
        playlistVideoRepository.save(entity);
        dto.setId(entity.getId());
        return new ApiResponseDTO(false,dto);
    }
    private PlaylistVideoEntity get(String playlistId, String  videoId){
        return playlistVideoRepository
                .findByPlaylistIdAndVideoIdAndVisibleTrue(playlistId,videoId)
                .orElseThrow(()-> new ItemNotFoundException("playlistVideo not found"));
    }

    public ApiResponseDTO delete(String playlistId, String videoId) {
        Integer profileId=SpringSecurityUtil.getProfileId();
        Optional<PlayListEntity>playlist=playListRepository.findPlaylist(playlistId, profileId);
        if(playlist.isEmpty()) throw new UnAuthorizedException("Playlist not belong to you!");
        int n=playlistVideoRepository.deleteVideo(playlistId,videoId);
        return n>0?new ApiResponseDTO("deleted",false):new ApiResponseDTO("not deleted",true);
    }
//4. Get Video list by playListId (video status published)
    public List<PlaylistVideoMapper> getByPlaylistId(String playlistId) {
        List<PlaylistVideoMapperI> mapper=playlistVideoRepository.getVideoListByPlaylistId(playlistId);
        return mapper.stream().map(s->{
            PlaylistVideoMapper videoMapper=new PlaylistVideoMapper();
            videoMapper.setPlaylistId(s.getPlaylistId());
            VideoDTO videoDTO=new VideoDTO();
            videoDTO.setId(s.getVideoId());
            AttachDTO previewDto=new AttachDTO();
            previewDto.setId(s.getPreviewAttachId());
            previewDto.setUrl(attachService.getUrl(s.getPreviewAttachId()));
            previewDto.setDuration(s.getAttachDuration());
            videoDTO.setPreviewAttach(previewDto);
            videoDTO.setTitle(s.getVideoTitle());
            videoMapper.setVideo(videoDTO);
            ChannelDTO channelDTO= new ChannelDTO();
            channelDTO.setId(s.getChannelId());
            channelDTO.setName(s.getChannelName());
            videoMapper.setChannel(channelDTO);
            videoMapper.setCreatedDate(s.getPVCreatedDate());
            videoMapper.setOrderNum(s.getPVOrderNum());
            return videoMapper;
        }).toList();
    }
    public PlayListDTO getByVideo(String videoId) {
        Optional<PlayListEntity> optional = playlistVideoRepository.findAllByVideoIdAndVisibleTrue(videoId);
        if (optional.isEmpty()) return null;
        PlayListDTO playListDTO = new PlayListDTO();
        playListDTO.setId(optional.get().getId());
        playListDTO.setName(optional.get().getName());
        return playListDTO;
    }
}
