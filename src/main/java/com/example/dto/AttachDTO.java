package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {
    private String id;
    private String originName;
    private String path;
    private Long size;
    private String extension;
    private LocalDateTime createdData;
    private String url;
    private Long duration;

    public AttachDTO() {
    }

    public AttachDTO(String id, String url) {
        this.id = id;
        this.url = url;
    }
    public AttachDTO(String id, String originName,Long size, String url) {
        this.id = id;
        this.originName=originName;
        this.size=size;
        this.url = url;
    }
}
