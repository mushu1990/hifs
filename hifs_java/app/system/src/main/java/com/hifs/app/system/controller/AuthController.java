package com.hifs.app.system.controller;

import com.hifs.app.system.domain.dto.AuthLoginDto;
import com.hifs.app.system.domain.entity.SystemAuthUserEntity;
import com.hifs.app.system.service.ISystemAuthUserService;
import com.hifs.hicore.core.domain.AjaxResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/auth")
public class AuthController {

    @Autowired
    public ISystemAuthUserService systemAuthUserService;


    @PostMapping("/login")
    @PreAuthorize("permitAll()") // 允许所有用户访问
    public AjaxResult login(@RequestBody @Valid AuthLoginDto dto) {
        System.out.println(dto);
        return AjaxResult.success(dto);
    }


    // 获取所有用户
    @GetMapping("/user/lists")
    public List<SystemAuthUserEntity> getAllUsers() {
        return systemAuthUserService.findAll();
    }

    // 获取通过id
    @GetMapping("/user/info/{id}")
    @ResponseBody
    public AjaxResult getUserById(@PathVariable Long id) {
        return AjaxResult.success(systemAuthUserService.findById(id));
    }
}
