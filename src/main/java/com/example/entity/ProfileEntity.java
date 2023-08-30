package com.example.entity;

import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseIntEntity {
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "surname",nullable = false)
    private String surname;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "image_id")
    private String imageId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id",insertable = false, updatable = false)
    private AttachEntity image;

    @Column(name = "role",nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileRole role;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language = Language.uz;

    @Column(name = "prt_id")
    private Integer prtId;

}
