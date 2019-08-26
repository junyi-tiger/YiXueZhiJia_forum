package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class LikePost implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long UID;
    private Long PID;
    Timestamp like_time = Timestamp.valueOf(LocalDateTime.now());
    public LikePost(Long UID, Long PID){
        this.UID = UID;
        this.PID = PID;
    }
}