package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private AttachService attachService;

    public ApiResponseDTO registration(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository
                .findByEmailAndVisibleTrue(dto.getEmail());
        if (optional.isPresent()) {
            ProfileEntity profileEntity = optional.get();
            if (!profileEntity.getStatus().equals(ProfileStatus.REGISTRATION)) {
                return new ApiResponseDTO("This email is already taken!", true);
            } else {
                profileRepository.deleteById(profileEntity.getId());
            }
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setImageId(dto.getImageId());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setStatus(ProfileStatus.REGISTRATION);
        profileRepository.save(entity);
        System.out.println(entity);
        mailSenderService.sendEmailVerification(entity); //send registration verification
        return new ApiResponseDTO("The verification link was send to your email", false);
    }

    public String emailVerification(String jwt) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        Optional<ProfileEntity> optional = profileRepository.findById(jwtDTO.getId());
        if (optional.isEmpty()) throw new ItemNotFoundException("Profile not found");
        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            throw new AppBadRequestException("verification link is expired!");
        }
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        return "you have been successfully verified!";
    }

    public ApiResponseDTO login(AuthDTO authDTO, Language language) {
        Optional<ProfileEntity> optional = profileRepository
                .findByEmailAndPasswordAndVisibleTrue(authDTO.getEmail(), MD5Util.encode(authDTO.getPassword()));
        if (optional.isEmpty()) return new ApiResponseDTO(true, "Profile not found");
        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)) {
            return new ApiResponseDTO(true, "Profile not active");
        }
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profileEntity.getId());
        profileDTO.setName(profileEntity.getName());
        profileDTO.setSurname(profileEntity.getSurname());
        profileDTO.setEmail(profileEntity.getEmail());
        profileDTO.setAttachDTO(attachService.getAttachWithURL(profileEntity.getImageId()));
        profileDTO.setRole(profileEntity.getRole());
        profileDTO.setJwt(JWTUtil.encode(profileEntity.getId(), profileEntity.getEmail()));
        profileEntity.setLanguage(language);
        profileRepository.save(profileEntity);
        return new ApiResponseDTO(false, profileDTO);
    }
}
