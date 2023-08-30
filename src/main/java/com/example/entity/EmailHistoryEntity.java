package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_history")
public class EmailHistoryEntity extends BaseStringEntity {
    @Column(name = "to_email")
    private String toEmail;
    @Column(name = "title")
    private String title;
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
}
