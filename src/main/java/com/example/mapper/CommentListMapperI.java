package com.example.mapper;

import java.time.LocalDateTime;

public interface CommentListMapperI {
    String getCommentId();
    String getContent();
    LocalDateTime getCreatedDate();
    Long getLikeCount();
    Long getDislikeCount();
    String getVideoId();
    String getPreviewAttachId();
    String getTitle();
    Long getDuration();
}
