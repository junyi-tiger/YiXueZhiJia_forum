package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Comment implements Serializable {
    /**
     * 帖子评论表，存储帖子的评论信息
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CID;//帖子评论唯一id
    @NotNull
    private Long PID;//评论的帖子id
    @NotNull
    private Long UID;//发表评论的用户id
    @NotNull
    private String CContent;//评论内容
    private int CLike_num;//评论的点赞数
    private int C_reply_num;//评论的回复数
    private Timestamp comment_time =  Timestamp.valueOf(LocalDateTime.now());//评论发表时间

    public Comment(Long PID, Long UID ,String CContent){
        this.PID = PID;
        this.UID = UID;
        this.CContent = CContent;
    }

}
