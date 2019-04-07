package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class LikeComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long UID;
    private Long CID;
    Timestamp like_time = Timestamp.valueOf(LocalDateTime.now());
    public LikeComment(Long UID, Long CID){
        this.UID = UID;
        this.CID = CID;
    }
}
