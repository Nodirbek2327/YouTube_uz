package com.example.dto;
import com.example.enums.VideoType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateVideoDTO {
    private String id;
    @NotBlank(message = "Field must have some value")
    private String previewAttachId;
    @NotBlank(message = "Field must have some value")
    private String title;
    @NotBlank(message = "Field must have some value")
    private Integer categoryId;
    @NotBlank(message = "Field must have some value")
    private VideoType type;
    private String description;
    @NotBlank(message = "Field must have some value")
    private List<Integer> videoTags;

}
