package com.example.service;

import com.example.dto.*;
import com.example.entity.PlayListEntity;
import com.example.exp.AppBadRequestException;
import com.example.mapper.*;
import com.example.repository.PlayListRepository;
import com.example.repository.PlaylistVideoRepository;
import com.example.util.SpringSecurityUtil;
import lombok.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayListService {
    @Autowired
    private PlayListRepository playListRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

//      1. Create Playlist (USER)
    public PlayListDTO create(PlayListDTO dto) {
        PlayListEntity entity=new PlayListEntity();
        Optional<PlayListEntity> optional=playListRepository
                .findByOrderNumberAndChannelIdAndVisibleTrue(dto.getOrderNumber(), dto.getChannelId());
        if(optional.isPresent()) throw new AppBadRequestException("Order number is exists");
        entity.setName(dto.getName());
        entity.setChannelId(dto.getChannelId());
        entity.setDescription(dto.getDescription());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setStatus(dto.getStatus());
        entity.setDescription(dto.getDescription());
        playListRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
//  2. Update Playlist(USER and OWNER)
    public ApiResponseDTO update(String playlistId, PlayListDTO dto) {
        Integer profileId= SpringSecurityUtil.getProfileId();
        Optional<PlayListEntity> optional=playListRepository.findPlaylist(playlistId,profileId);
        if(optional.isEmpty()) return new ApiResponseDTO("playlist not found",true);
        PlayListEntity entity =optional.get();
        entity.setName(dto.getName());
        entity.setChannelId(dto.getChannelId());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setOrderNumber(dto.getOrderNumber());
        playListRepository.save(entity);
        dto.setId(playlistId);
        return new ApiResponseDTO(false,dto);
    }
    // 3. Change Playlist Status (USER and OWNER)
    public ApiResponseDTO updateStatus(String playlistId, AccessLevel status) {
        Integer profileId= SpringSecurityUtil.getProfileId();
        Optional<PlayListEntity> optional=playListRepository.findPlaylist(playlistId,profileId);
        if(optional.isEmpty()) return new ApiResponseDTO("playlist not found",true);
        PlayListEntity entity=optional.get();
        entity.setStatus(status);
        playListRepository.save(entity);
        PlayListDTO dto=new PlayListDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(dto.getDescription());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setChannelId(entity.getChannelId());
        dto.setStatus(entity.getStatus());
        return new ApiResponseDTO(false,dto);
    }
    // 4. Delete Playlist (USER and OWNER, ADMIN)
    public ApiResponseDTO delete(String playlistId) {
        Integer profileId= SpringSecurityUtil.getProfileId();
        Optional<PlayListEntity> optional=playListRepository.findPlaylist(playlistId,profileId);
        if(optional.isEmpty()) return new ApiResponseDTO("playlist not found",true);
        int n=playListRepository.deletePlaylist(playlistId);
        return n>0? new ApiResponseDTO("playlist deleted",false):
                new ApiResponseDTO("playlist not deleted",true);
    }

    public PageImpl<PlayListDTO> pagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<PlaylistMapperI>pageObj=playListRepository.pagination(pageable);
        List<PlayListDTO> dtoList=pageObj.getContent().stream().map(s->getPlaylistInfo(s)).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
//        6. Playlist List By UserId (order by order number desc) (ADMIN)
    public List<PlayListDTO> getByUserId(Integer userId) {
      return playListRepository.getByUserId(userId).stream().map(this::getPlaylistInfo).toList();
    }
    private PlayListDTO getPlaylistInfo(PlaylistMapperI s){
        PlayListDTO dto=new PlayListDTO();
        dto.setId(s.getPlaylistId());
        dto.setName(s.getPlaylistName());
        dto.setDescription(s.getPlaylistDescription());
        dto.setStatus(s.getPlaylistStatus());
        dto.setOrderNumber(s.getPlaylistOrderNum());
        ChannelDTO channelDTO=new ChannelDTO();
        channelDTO.setId(s.getChannelId());
        channelDTO.setName(s.getChannelName());
        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setId(s.getChannelPhotoId());
        attachDTO.setUrl(attachService.getUrl(s.getChannelPhotoId()));
        channelDTO.setPhoto(attachDTO);
        dto.setChannelDTO(channelDTO);
        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setId(s.getProfileId());
        profileDTO.setName(s.getProfileName());
        profileDTO.setSurname(s.getProfileSurname());
        AttachDTO profilePhoto=new AttachDTO();
        profilePhoto.setId(s.getProfilePhotoId());
        profilePhoto.setUrl(attachService.getUrl(s.getProfilePhotoId()));
        profileDTO.setAttachDTO(profilePhoto);
        dto.setProfileDTO(profileDTO);
        return dto;
    }
    // 7. Get User Playlist (order by order number desc) (murojat qilgan user ni)
    public List<PlaylistShortInfoDTO> getOwnPlayList() {
        Integer profileId=SpringSecurityUtil.getProfileId();
        List<PlaylistShortInfoMapperI>mapperList=playListRepository.getOwnPlaylist(profileId);
       return getShortInfo(mapperList);
    }

    public List<PlaylistShortInfoDTO> getByChannelId(String channelId) {
        List<PlaylistShortInfoMapperI>mapperList=playListRepository.getByChannelId(channelId);
        return getShortInfo(mapperList);
    }
    private List<PlaylistShortInfoDTO> getShortInfo(List<PlaylistShortInfoMapperI> mapperList){
       return mapperList.stream().map(s->{
            PlaylistShortInfoDTO dto=new PlaylistShortInfoDTO();
            dto.setPlayListId(s.getPlaylistId());
            dto.setPlayListName(s.getPlaylistName());
            dto.setCreatedDate(s.getPlaylistCreatedDate());
            ChannelDTO channelDTO=new ChannelDTO();
            channelDTO.setId(s.getChannelId());
            channelDTO.setName(s.getChannelName());
            dto.setChannel(channelDTO);
            List<VideoShortInfoMapper>list=playlistVideoRepository.findVideoByPlaylistId(s.getPlaylistId());
            if(list.size()>1){
                dto.setVideoList(List.of(list.get(0),list.get(1)));
            }else {
                dto.setVideoList(list);
            }
            dto.setVideoCount(list.size());
            return dto;
        }).toList();
    }
//     9. Get Playlist by id
//    id,name,video_count, total_view_count (shu play listdagi videolarni ko'rilganlar soni),
//            last_update_date
    public PlaylistMapper getById(String playlistId) {
        PlaylistMapperI2 mapper=playlistVideoRepository.getInfoById(playlistId);
        return new PlaylistMapper(mapper.getPlaylistId(), mapper.getPlaylistName(),
                mapper.getVideoCount(), mapper.getTotalViewCount(), mapper.getLastUpdateDate());
    }
}
