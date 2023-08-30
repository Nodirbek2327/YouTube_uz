package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "video_tags")
public class VideoTagsEntity extends BaseStringEntity {

    @Column(name = "video_id")
    private String videoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id",insertable = false,updatable = false)
    private VideoEntity video;

    @Column(name = "tag_id")
    private Integer tagId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id",insertable = false,updatable = false)
    private TagEntity tag;

}
