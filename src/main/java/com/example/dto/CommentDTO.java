package com.example.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private String id;
    private Integer profileId;
    private String videoId;
    private String content;
    private String replyId;
    private Long likeCount;
    private Long dislikeCount;
    private LocalDateTime createdDate;
    private VideoDTO video;
    private ProfileDTO profile;
}
