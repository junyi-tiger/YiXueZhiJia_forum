package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Post {
    /**
     * 帖子表，存储帖子信息
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PID;//帖子唯一id
    @NotNull
    private Long UId;//帖子所属用户的id
    @NotNull
    private String PTitle;//帖子主题
    @NotNull
    private String PContent;//帖子内容
    private int PLike_num;//帖子点赞数
    private int PComments;//帖子评论数
    private int PCollection_num;//帖子收藏数

    private int PState;//帖子状态
    private int PTag;//帖子标签
    private Timestamp Post_time = Timestamp.valueOf(LocalDateTime.now());//帖子发表时间

    public Post(Long UId, String PTitle, String PContent){
        this.UId = UId;
        this.PTitle = PTitle;
        this.PContent = PContent;
    }
}