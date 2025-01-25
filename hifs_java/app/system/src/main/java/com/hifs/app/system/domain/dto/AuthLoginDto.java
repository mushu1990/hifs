package com.hifs.app.system.domain.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthLoginDto {
    @NotEmpty(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名必须在2-10个字符")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Size(message = "密码必须在2-10个字符")
    private String password;

}
