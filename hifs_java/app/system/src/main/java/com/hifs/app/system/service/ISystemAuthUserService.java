package com.hifs.app.system.service;


import com.hifs.app.system.domain.entity.SystemAuthUserEntity;

import java.util.List;
import java.util.Optional;


public interface ISystemAuthUserService {

    List<SystemAuthUserEntity> findAll();

    Optional<SystemAuthUserEntity> findById(Long id);

    void addUser(SystemAuthUserEntity user);

    void updateUser(SystemAuthUserEntity user);

    void deleteUser(Long id);
}
