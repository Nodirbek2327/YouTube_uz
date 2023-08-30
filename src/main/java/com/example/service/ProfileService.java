package com.example.service;

import com.example.config.CustomUserDetails;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.AttachEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private MailSenderService mailSenderService;

    public ProfileDTO create(ProfileDTO profileDTO, Integer prtId) {
        if (profileDTO.getRole() == null) {
            throw new AppBadRequestException("Role is null");
        }
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(profileDTO.getName());
        profileEntity.setSurname(profileDTO.getSurname());
        profileEntity.setPassword(MD5Util.encode(profileDTO.getPassword()));
        profileEntity.setRole(profileDTO.getRole());
        profileEntity.setEmail(profileDTO.getEmail());
        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileEntity.setImageId(profileDTO.getImageId());
        profileEntity.setPrtId(prtId);
        profileRepository.save(profileEntity);
        profileDTO.setId(profileEntity.getId());
        profileDTO.setCreateDate(profileEntity.getCreatedDate());
        return profileDTO;
    }

    public ProfileDTO getProfileDetail() {
        CustomUserDetails userDetails = SpringSecurityUtil.getCurrentUser();
        Optional<ProfileEntity> optional = profileRepository.findById(userDetails.getProfile().getId());
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Profile not found");
        }
        ProfileEntity profileEntity = optional.get();
        String url = null;
        if (profileEntity.getImageId() != null) {
            url = attachService.getUrl(profileEntity.getImageId());
        }
        return new ProfileDTO(profileEntity.getId(), profileEntity.getName(),
                profileEntity.getSurname(), profileEntity.getEmail(), url);
    }

    public ProfileDTO updateMainPhoto(String newPhotoId, Integer prtId ) {
        Optional<ProfileEntity> optional = profileRepository.findById(prtId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Profile not found");
        }
        if (attachService.get(newPhotoId)==null){
            throw new ItemNotFoundException("new attach not found");
        }
        ProfileEntity entity = optional.get();
        String oldPhotoId = entity.getImageId();
        entity.setImageId(newPhotoId);
        profileRepository.save(entity);
        attachService.delete(oldPhotoId);
        return getProfileDetail();
    }

    public ProfileDTO updateProfileDetail(String name, String surname, Integer prtId) {
        Optional<ProfileEntity> optional = profileRepository.findById(prtId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Profile Not Found");
        }
        ProfileEntity entity = optional.get();
        entity.setName(name);
        entity.setSurname(surname);
        profileRepository.save(entity);
        return new ProfileDTO(entity.getName(), entity.getSurname());
    }

    public Boolean changePassword(String password, Integer prtId) {
        Optional<ProfileEntity> optional = profileRepository.findById(prtId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Profile Not Found");
        }
        ProfileEntity entity = optional.get();
        if (entity.getPassword().equals(MD5Util.encode(password))) {
            throw new AppBadRequestException("The same as the previous password");
        }
        entity.setPassword(MD5Util.encode(password));
        profileRepository.save(entity);
        return true;
    }

    public ProfileDTO sendMSGtoEmail(String email,ProfileEntity profile) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(email);
        if (optional.isPresent()) {
            throw new AppBadRequestException("This email already used");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setId(profile.getId());
        entity.setName(profile.getName());
        entity.setEmail(email);
        mailSenderService.sendEmailUpdateGmail(entity);
        return new ProfileDTO(profile.getId(), email);
    }

    public String updateEmail(String jwt) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        Optional<ProfileEntity> optional = profileRepository.findById(jwtDTO.getId());
        ProfileEntity entity = optional.get();
        entity.setEmail(jwtDTO.getEmail());
        profileRepository.save(entity);
        return "email succesfully updated";
    }


    public ProfileDTO getProfile(Integer profileId) {
        Optional<ProfileEntity> optional = profileRepository.findAllByIdAndVisibleTrue(profileId);
        if (optional.isEmpty()) return null;
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(optional.get().getId());
        profileDTO.setName(optional.get().getName());
        profileDTO.setSurname(optional.get().getSurname());
        return profileDTO;
    }
}
