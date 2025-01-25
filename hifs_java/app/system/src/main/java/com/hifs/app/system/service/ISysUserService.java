package com.hifs.app.system.service;

import com.hifs.hicore.core.domain.entity.SysUser;
import org.springframework.stereotype.Service;

@Service
public interface ISysUserService {
    void updateUserProfile(SysUser sysUser);

    boolean checkUserNameUnique(SysUser sysUser);

    boolean registerUser(SysUser sysUser);

    SysUser selectUserByUserName(String username);
}
