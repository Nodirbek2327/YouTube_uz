package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "playlist_video")
public class PlaylistVideoEntity extends BaseStringEntity {
    @Column(name = "playlist_id")
    private String playlistId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", insertable = false, updatable = false)
    private PlayListEntity playList;
    @Column(name = "video_id")
    private String videoId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;
    @Column(name = "order_number")
    private Integer orderNumber;
}
