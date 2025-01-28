package com.hifs.app.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@TableName("hi_system_auth_user")
@Table(name = "hi_system_auth_user")
public class SystemAuthUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private int status;
    private int loginFailCount;


}
