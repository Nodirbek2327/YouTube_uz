package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class CommentEntity extends BaseStringEntity{
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "video_id")
    private String videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id",insertable = false,updatable = false)
    private VideoEntity video;
    @Column(name = "content",columnDefinition = "TEXT")
    private String content;
    @Column(name = "reply_id")
    private String replyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id",insertable = false,updatable = false)
    private CommentEntity comment;
    @Column
    private Long likeCount=0L;
    @Column
    private Long dislikeCount=0L;
}
