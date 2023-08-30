package com.example.service;

import com.example.dto.*;
import com.example.entity.CommentEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.exp.AppMethodNotAllowedException;
import com.example.exp.ItemNotFoundException;
import com.example.mapper.CommentInfoMapperI;
import com.example.mapper.CommentListMapperI;
import com.example.repository.CommentRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private AttachService attachService;
//1. Crate Comment (USER)
    public CommentDTO create(CommentCreateDTO dto) {
        Integer prtId= SpringSecurityUtil.getProfileId();
        CommentEntity entity= new CommentEntity();
        entity.setProfileId(prtId);
        entity.setContent(dto.getContent());
        entity.setReplyId(dto.getReplyId());
        entity.setVideoId(dto.getVideoId());
        System.out.println(dto.getVideoId());
        commentRepository.save(entity);
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(entity.getId());
        commentDTO.setLikeCount(entity.getLikeCount());
        commentDTO.setDislikeCount(entity.getDislikeCount());
        commentDTO.setVideoId(dto.getVideoId());
        commentDTO.setContent(dto.getContent());
        commentDTO.setReplyId(dto.getReplyId());
        commentDTO.setProfileId(prtId);
        return commentDTO;
    }
// 2. Update Comment (USER AND OWNER)
    public CommentDTO update(String commentId, CommentUpdateDTO dto) {
        ProfileEntity profile=SpringSecurityUtil.getCurrentUser().getProfile();
        CommentEntity entity=get(commentId,profile.getLanguage());
        if(!entity.getProfileId().equals(profile.getId())){
            throw new AppMethodNotAllowedException(resourceBundleService
                    .getMessage("method.not.allowed",profile.getLanguage()));
        }
        entity.setContent(dto.getContent());
        commentRepository.save(entity);
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(entity.getId());
        commentDTO.setLikeCount(entity.getLikeCount());
        commentDTO.setDislikeCount(entity.getDislikeCount());
        commentDTO.setVideoId(entity.getVideoId());
        commentDTO.setContent(dto.getContent());
        commentDTO.setReplyId(entity.getReplyId());
        commentDTO.setProfileId(profile.getId());
        return commentDTO;
    }
    // 3. Delete Comment (USER AND OWNER, ADMIN)
    public ApiResponseDTO delete(String commentId) {
        ProfileEntity profile=SpringSecurityUtil.getCurrentUser().getProfile();
        CommentEntity entity=get(commentId,profile.getLanguage());
        if(profile.getRole().equals(ProfileRole.ROLE_USER)){
            if(!entity.getProfileId().equals(profile.getId())){
                throw new AppMethodNotAllowedException(resourceBundleService
                        .getMessage("method.not.allowed",profile.getLanguage()));

            }
        }
        int n=commentRepository.deleteComment(commentId);
        return n>0?new ApiResponseDTO(false,"deleted"):new ApiResponseDTO(true,"not deleted");
    }
    //4. Comment List Pagination (ADMIN)
    public PageImpl<CommentDTO> pagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<CommentEntity>pageObj=commentRepository.findAllByVisibleTrue(pageable);
        List<CommentDTO> dtoList=pageObj.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    // 5. Comment List By profileId(ADMIN)
    public List<CommentDTO> commentList(Integer profileId) {
        List<CommentListMapperI>mapper=commentRepository.getByProfileId(profileId);
        return mapper.stream().map(s->{
            CommentDTO commentDTO= new CommentDTO();
            commentDTO.setId(s.getCommentId());
            commentDTO.setContent(s.getContent());
            commentDTO.setCreatedDate(s.getCreatedDate());
            commentDTO.setLikeCount(s.getLikeCount());
            commentDTO.setDislikeCount(s.getDislikeCount());
            VideoDTO videoDTO= new VideoDTO();
            videoDTO.setId(s.getVideoId());
            videoDTO.setPreviewAttachId(s.getPreviewAttachId());
            videoDTO.setTitle(s.getTitle());
            AttachDTO attachDTO= new AttachDTO();
            attachDTO.setDuration(s.getDuration());
            videoDTO.setAttach(attachDTO);
            commentDTO.setVideo(videoDTO);
            return commentDTO;
        }).toList();
    }
    //  7. Comment List by videoId
    public List<CommentDTO> commentByVideoId(String videoId) {
    return toShortInfo(commentRepository.getByVideoId(videoId));
    }
    // 8. Get Comment Replied Comment by comment Id
    public List<CommentDTO> repliedComment(String commentId) {
       return toShortInfo(commentRepository.repliedComment(commentId));
    }
    private List<CommentDTO>toShortInfo(List<CommentInfoMapperI> list){
        return list.stream().map(s->{
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(s.getCommentId());
            commentDTO.setContent(s.getContent());
            commentDTO.setCreatedDate(s.getCreatedDate());
            commentDTO.setLikeCount(s.getLikeCount());
            commentDTO.setDislikeCount(s.getDislikeCount());
            commentDTO.setVideoId(s.getVideoId());
            ProfileDTO profileDTO= new ProfileDTO();
            profileDTO.setId(s.getProfileId());
            profileDTO.setName(s.getProfileName());
            profileDTO.setSurname(s.getProfileSurname());
            AttachDTO attachDTO= new AttachDTO();
            attachDTO.setId(s.getProfilePhotoId());
            attachDTO.setUrl(attachService.getUrl(s.getProfilePhotoId()));
            profileDTO.setAttachDTO(attachDTO);
            commentDTO.setProfile(profileDTO);
            return commentDTO;
        }).toList();
    }
    private CommentDTO toDto(CommentEntity entity){
        CommentDTO dto= new CommentDTO();
        dto.setProfileId(entity.getProfileId());
        dto.setVideoId(entity.getVideoId());
        dto.setLikeCount(entity.getLikeCount());
        dto.setDislikeCount(entity.getDislikeCount());
        dto.setContent(entity.getContent());
        dto.setReplyId(entity.getReplyId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
    private CommentEntity get(String commentId, Language language){
        return commentRepository.findByIdAndVisibleTrue(commentId)
                .orElseThrow(()->new ItemNotFoundException(resourceBundleService.getMessage("item.not.found",language)));
    }

}
