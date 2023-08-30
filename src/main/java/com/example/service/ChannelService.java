package com.example.service;

import com.example.config.CustomUserDetails;
import com.example.dto.*;
import com.example.entity.ChannelEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ChannelStatus;
import com.example.enums.ProfileRole;
import com.example.exp.AppBadRequestException;
import com.example.dto.ChannelDTO;
import com.example.entity.ChannelEntity;
import com.example.repository.ChannelRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ProfileService profileService;

    public ApiResponeDTO create(ChannelDTO channelDTO) {
        ChannelEntity checkToName = channelRepository.findByName(channelDTO.getName());
        if (checkToName != null) {
            throw new AppBadRequestException("This name is reserved");
        }
        ChannelEntity entity = new ChannelEntity();
        entity.setName(channelDTO.getName());
        CustomUserDetails userDetails = SpringSecurityUtil.getCurrentUser();
        entity.setProfileId(userDetails.getProfile().getId());
        entity.setBannerId(channelDTO.getBannerId());
        entity.setImageId(channelDTO.getImageId());
        entity.setDescription(channelDTO.getDescription());
        entity.setStatus(ChannelStatus.ACTIVE);
        channelRepository.save(entity);
        channelDTO.setProfileId(entity.getProfileId());
        channelDTO.setId(entity.getId());
        channelDTO.setCreatedDate(entity.getCreatedDate());
        return new ApiResponeDTO(true, channelDTO);
    }

    public ApiResponeDTO updateName(String oldName, String newName) {
        CustomUserDetails userDetails = SpringSecurityUtil.getCurrentUser();
        ChannelEntity checkToName = channelRepository.findByName(newName);
        if (checkToName != null) {
            throw new AppBadRequestException("This name is reserved");
        }
        ChannelEntity entity = channelRepository.findByNameAndProfileId(oldName, userDetails.getProfile().getId());
        if (entity == null) {
            throw new AppBadRequestException("Channel not found");
        }
        if (oldName != newName) {
            entity.setName(newName);
        }
        channelRepository.save(entity);
        return new ApiResponeDTO(true, "name updated");
    }

    public ApiResponeDTO updatePhoto(String imageId, String name) {
        CustomUserDetails userDetails = SpringSecurityUtil.getCurrentUser();
        ChannelEntity entity = channelRepository.findByNameAndProfileId(name, userDetails.getProfile().getId());
        if (entity == null) {
            throw new AppBadRequestException("Channel not found");
        }
        entity.setImageId(imageId);
        channelRepository.save(entity);
        return new ApiResponeDTO(true, "photo updated");
    }

    public ApiResponeDTO updateBanner(String bannerId, String name) {
        CustomUserDetails userDetails = SpringSecurityUtil.getCurrentUser();
        ChannelEntity entity = channelRepository.findByNameAndProfileId(name, userDetails.getProfile().getId());
        if (entity == null) {
            throw new AppBadRequestException("Channel not found");
        }
        entity.setBannerId(bannerId);
        channelRepository.save(entity);
        return new ApiResponeDTO(true, "banner is updated");
    }

    public PageImpl<ChannelDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<ChannelEntity> pageObj = channelRepository.findAll(pageable);
        List<ChannelDTO> dtoList = pageObj.stream().map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
    }

    public ApiResponeDTO getChanelById(String id) {
        Optional<ChannelEntity> optional = channelRepository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponeDTO(false, "Channel not found");
        }
        return new ApiResponeDTO(true, toDTO(optional.get()));
    }

    public ApiResponeDTO changeStatus(String id) {
        CustomUserDetails userDetails = SpringSecurityUtil.getCurrentUser();
        ChannelEntity entity = null;
        if (userDetails.getProfile().getRole().equals(ProfileRole.ROLE_USER)) {
            entity = channelRepository.findByIdAndProfileId(id, userDetails.getProfile().getId());
        } else {
            Optional<ChannelEntity> optional = channelRepository.findById(id);
            entity = optional.get();
        }

        if (entity == null) {
            return new ApiResponeDTO(false, "Channel not found");
        }
        if (entity.getStatus().equals(ChannelStatus.ACTIVE)) {
            entity.setStatus(ChannelStatus.BLOCK);
            channelRepository.save(entity);
        } else {
            entity.setStatus(ChannelStatus.ACTIVE);
            channelRepository.save(entity);
        }
        return new ApiResponeDTO(true, entity.getStatus());
    }

    public ApiResponeDTO getChannelList() {
        CustomUserDetails userDetails = SpringSecurityUtil.getCurrentUser();
        List<ChannelEntity> entityList = channelRepository.findByProfileId(userDetails.getProfile().getId());
        if (entityList.isEmpty()) {
            return new ApiResponeDTO(false, "Chanels not found");
        }
        List<ChannelDTO> dtoList = entityList.stream().map(this::toDTO).collect(Collectors.toList());
        return new ApiResponeDTO(true, dtoList);
    }

    public ChannelDTO toDTO(ChannelEntity entity) {
        ChannelDTO dto = new ChannelDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setProfileId(entity.getProfileId());
        dto.setBannerId(entity.getBannerId());
        dto.setImageId(entity.getImageId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public ChannelDTO getChannel(String channelId) {
        Optional<ChannelEntity> optional = channelRepository.findById(channelId);
        if (optional.isEmpty()) return null;
        ChannelEntity entity = optional.get();
        ChannelDTO dto = new ChannelDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setUrl(attachService.getUrl(entity.getImageId()));
        dto.setPhoto(attachDTO);
        return dto;
    }


    public ProfileDTO getOwner(String channelId) {
        Optional<ChannelEntity> optional = channelRepository.findAllByIdAndVisibleTrue(channelId);
        return optional.map(channelEntity -> profileService.getProfile(channelEntity.getProfileId())).orElse(null);
    }
}
