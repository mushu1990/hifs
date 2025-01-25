package com.hifs.app.system.domain.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hi_system_auth_user")
public class SystemAuthUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
}
