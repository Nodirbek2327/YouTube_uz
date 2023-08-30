package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseIntDTO {
    private Integer id;
    private Boolean visible;
    private LocalDateTime created_date;
}
