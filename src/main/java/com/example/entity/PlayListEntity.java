package com.example.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Getter
@Setter
@Entity
@Table(name = "playlist")
public class PlayListEntity extends BaseStringEntity {
    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", updatable = false,insertable = false)
    private ChannelEntity channelEntity;
    @Column()
    private String name;
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column
    private AccessLevel status;
    @Column(name = "order_number")
    private Integer orderNumber;

}
