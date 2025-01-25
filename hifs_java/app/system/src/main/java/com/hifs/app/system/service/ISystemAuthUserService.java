package com.hifs.app.system.service;


import com.hifs.app.system.domain.entity.SystemAuthUserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISystemAuthUserService {


    public List<SystemAuthUserEntity> findAll();

    public List<SystemAuthUserEntity> findAll1();

    public SystemAuthUserEntity findById(Long id);

    public void createUser(SystemAuthUserEntity user);

    public void updateUser(SystemAuthUserEntity user);

    public void deleteUser(Long id);
}
