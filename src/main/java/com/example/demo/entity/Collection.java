package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 帖子收藏表
 */
@Data
@Entity
@NoArgsConstructor
public class Collection {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long UID;//收藏者id
    private Long PID;//帖子id
    private Timestamp collect_time = Timestamp.valueOf(LocalDateTime.now());
    public Collection(Long UID, Long PID){
        this.UID = UID;
        this.PID = PID;
    }
}