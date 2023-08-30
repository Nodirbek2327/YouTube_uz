package com.example.entity;

import com.example.enums.ChannelStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "channel")
public class ChannelEntity extends BaseStringEntity{
    @Column(name = "name")
    private String name;

    @Column(name = "image_id")
    private String imageId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id",insertable = false, updatable = false)
    private AttachEntity image;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private ChannelStatus status;

    @Column(name = "banner_id")
    private String bannerId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id",insertable = false, updatable = false)
    private AttachEntity banner;


    @Column(name = "profile_id",nullable = false)
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",insertable = false,updatable = false)
    private ProfileEntity profile;






}
