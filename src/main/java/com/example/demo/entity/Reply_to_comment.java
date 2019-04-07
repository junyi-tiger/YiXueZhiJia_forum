package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Reply_to_comment {
    /**
     * 帖子评论的回复表,存储关于评论的回复
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RID;//唯一Id
    @NotNull
    private Long CID;//评论的id
    @NotNull
    private Long UID;//回复者的id
    @NotNull
    private String RContent;//回复的内容
    private Timestamp reply_time = Timestamp.valueOf(LocalDateTime.now());//回复发表的时间

    public Reply_to_comment(Long CID, Long UID, String RContent){
        this.CID = CID;
        this.UID = UID;
        this.RContent = RContent;
    }
}