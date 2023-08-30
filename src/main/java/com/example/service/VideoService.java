package com.example.service;

import com.example.dto.*;
import com.example.entity.PlayListEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.VideoEntity;
import com.example.enums.ProfileRole;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.CustomRepository;
import com.example.repository.VideoRepository;
import com.example.util.SpringSecurityUtil;
import lombok.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoTagService videoTagService;
    @Autowired
    private CustomRepository customRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PlaylistVideoService playlistVideoService;


    public VideoDTO create(VideoDTO videoDTO) {
        VideoEntity entity = new VideoEntity();
        entity.setPreviewAttachId(videoDTO.getPreviewAttachId());
        entity.setTitle(videoDTO.getTitle());
        entity.setCategoryId(videoDTO.getCategoryId());
        entity.setAttachId(videoDTO.getAttachId());
        entity.setStatus(videoDTO.getStatus());
        entity.setType(videoDTO.getType());
        entity.setDescription(videoDTO.getDescription());
        entity.setChannelId(videoDTO.getChannelId());
        videoRepository.save(entity);
        videoTagService.create(entity.getId(), videoDTO.getVideoTags());
        videoDTO.setId(entity.getId());
        return videoDTO;
    }

    public UpdateVideoDTO update(String videoId, UpdateVideoDTO updateVideoDTO) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getProfile().getId();
        Optional<VideoEntity> optional = videoRepository.getByIdAndProfileId(videoId, profileId);
        if (optional.isEmpty()) throw new AppBadRequestException("Video not found");
        VideoEntity entity = optional.get();
        entity.setPreviewAttachId(updateVideoDTO.getPreviewAttachId());
        entity.setTitle(updateVideoDTO.getTitle());
        entity.setCategoryId(updateVideoDTO.getCategoryId());
        entity.setType(updateVideoDTO.getType());
        entity.setDescription(updateVideoDTO.getDescription());
        videoRepository.save(entity);
        videoTagService.merge(entity.getId(), updateVideoDTO.getVideoTags());
        updateVideoDTO.setId(entity.getId());
        return updateVideoDTO;
    }

    public String changeStatus(String videoId, AccessLevel status) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getProfile().getId();
        Optional<VideoEntity> optional = videoRepository.getByIdAndProfileId(videoId, profileId);
        if (optional.isEmpty()) throw new AppBadRequestException("Video not found");
        videoRepository.updateStatus(videoId, status);
        return "Success";
    }

    public boolean increaseViewCount(String videoId) {
        int effectRow = videoRepository.increaseViewCount(videoId);
        return effectRow == 1;
    }

    public PageImpl<VideoDTO> getPaginationByCategory(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<VideoEntity> entityPages = videoRepository.findAllByCategoryIdAndVisibleTrue(categoryId, pageable);
        return new PageImpl<>(entityPages.getContent().stream().map(this::videoShortInfo).toList(), pageable, entityPages.getTotalElements());
    }

    public List<VideoDTO> search(String title) {
        List<VideoEntity> entityList = videoRepository.searchByTitle(title);
        return entityList.stream().map(this::videoShortInfo).toList();
    }

    public PageImpl<VideoDTO> getByTag(Integer tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<VideoEntity> entityPages = customRepository.getVideoByTagId(tagId, page, size);
        int videoCount = videoRepository.getVideoCount(tagId);
        return new PageImpl<>(entityPages.stream().map(this::videoShortInfo).toList(), pageable, videoCount);
    }

    public VideoDTO getById(String videoId) {
        Optional<VideoEntity> optional = videoRepository.findAllByIdAndVisibleTrue(videoId);
        if (optional.isEmpty()) throw new ItemNotFoundException("Video not found");
        VideoEntity entity = optional.get();
        ProfileEntity profile = SpringSecurityUtil.getCurrentUser().getProfile();
        if (profile.getRole().equals(ProfileRole.ROLE_ADMIN) ||
                entity.getChannel().getProfileId().equals(profile.getId())) {
            return videoFullInfo(entity);
        }
        if (entity.getStatus().equals(AccessLevel.PUBLIC)) {
            return videoFullInfo(entity);
        }
        return new VideoDTO();
    }

    public List<Object> getPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoEntity> entityPages = videoRepository.findAll(pageable);
        List<Object> list = new LinkedList<>();
        entityPages.getContent().forEach(entity -> {
            Map<String, Object> map = new HashMap<>();
            map.put("video", videoShortInfo(entity));
            map.put("owner", channelService.getOwner(entity.getChannelId()));
            map.put("playlist", playlistVideoService.getByVideo(entity.getId()));
            list.add(map);
        });
        return list;
    }

    public PageImpl<VideoDTO> getChannelVideoPagination(String channelId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<VideoEntity> entityPages = videoRepository.findAllByChannelIdAndVisibleTrue(channelId, pageable);
        return new PageImpl<>(entityPages.getContent().stream().map(this::videoPlayListInfo).toList(), pageable, entityPages.getTotalElements());
    }

    public VideoDTO videoShortInfo(VideoEntity entity) {
        VideoDTO dto = new VideoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPreviewAttach(attachService.getAttachWithURL(entity.getAttachId()));
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setChannel(channelService.getChannel(entity.getChannelId()));
        dto.setViewCount(entity.getViewCount());
        dto.setDuration(entity.getAttach().getDuration());
        return dto;
    }


    public VideoDTO videoFullInfo(VideoEntity entity) {
        VideoDTO dto = new VideoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPreviewAttach(attachService.getAttachWithURL(entity.getPreviewAttachId()));
        dto.setAttach(attachService.getAttachVideo(entity.getAttachId()));
        dto.setCategory(categoryService.getCategory(entity.getCategoryId()));
        dto.setVideoTag(videoTagService.get(entity.getId()));
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setChannel(channelService.getChannel(entity.getChannelId()));
        dto.setViewCount(entity.getViewCount());
        dto.setSharedCount(entity.getSharedCount());
        dto.setLikeCount(entity.getLikeCount());
        dto.setDislikeCount(entity.getDislikeCount());
        // TODO isUserLiked IsUserDisliked
        dto.setDuration(entity.getAttach().getDuration());
        return dto;
    }

    public VideoDTO videoPlayListInfo(VideoEntity entity) {
        VideoDTO dto = new VideoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPreviewAttach(attachService.getAttachWithURL(entity.getAttachId()));
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setViewCount(entity.getViewCount());
        dto.setDuration(entity.getAttach().getDuration());
        return dto;
    }


}
