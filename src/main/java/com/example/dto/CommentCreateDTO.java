package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDTO {
    @NotBlank(message = "videoId is required")
    private String videoId;
    @NotBlank(message = "content is required")
    private String content;
    private String replyId;
}
