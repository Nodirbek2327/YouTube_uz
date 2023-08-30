package com.example.dto;


import com.example.enums.VideoType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTO {

    private String id;
    @NotBlank(message = "Field must have some value")
    private String previewAttachId;
    private AttachDTO previewAttach;
    @NotBlank(message = "Field must have some value")
    private String title;
    @NotBlank(message = "Field must have some value")
    private Integer categoryId;
    private CategoryDTO category;
    @NotBlank(message = "Field must have some value")
    private String attachId;
    private AttachDTO attach;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    @NotBlank(message = "Field must have some value")
    private AccessLevel status;
    @NotBlank(message = "Field must have some value")
    private VideoType type;
    private Integer viewCount;
    private Integer sharedCount;
    private String description;
    private Long duration;
    @NotBlank(message = "Field must have some value")
    private String channelId;
    private ChannelDTO channel;
    private Integer likeCount;
    private Integer dislikeCount;
    @NotBlank(message = "Field must have some value")
    private List<Integer> videoTags;
    private List<TagDTO> videoTag;

}
