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
public class Employee {
    /**
     * 样例表（职工）
     * ——for testing only
     */
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private @NotNull @Column(unique = true) String name;
    private @NotNull String role;
    private Timestamp register_time = Timestamp.valueOf(LocalDateTime.now());//职员注册时间

    public Employee(String name, String role){
        this.name = name;
        this.role = role;
    }
}
