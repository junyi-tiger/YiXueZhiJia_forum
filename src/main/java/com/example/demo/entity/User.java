package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class User {
    /**
     * 用户表，存储用户信息
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UId;//用户唯一id
    @NotNull @Column(unique = true)
    private String UName;//用户名
    @NotNull
    private String UPassword;//用户密码
    @NotNull
    private String UEmail;//用户电子邮箱
    @NotNull
    private int USex;//用户性别（0：女，1：男，2：未知）
    private String UHead;//用户头像地址
    private int UState;//用户状态
    private int URole;//用户角色
    private Timestamp register_time = Timestamp.valueOf(LocalDateTime.now());//用户注册时间

    public User(String UName, String UPassword, String UEmail, int USex){
        this.UName = UName;
        this.UPassword = UPassword;
        this.UEmail = UEmail;
        this.USex = USex;
    }
}
