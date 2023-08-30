package com.example.mapper;

import java.time.LocalDateTime;

public interface CommentInfoMapperI {
    String getCommentId();
    String getContent();
    LocalDateTime getCreatedDate();
    Long getLikeCount();
    Long getDislikeCount();
    String getVideoId();
    Integer getProfileId();
    String getProfileName();
    String getProfileSurname();
    String  getProfilePhotoId();
}
