package com.hifs.app.system.service.impl;

import com.hifs.app.system.domain.entity.SystemAuthUserEntity;
import com.hifs.app.system.domain.mapper.SystemAuthUserMapper;
import com.hifs.app.system.repository.SystemAuthUserRepository;
import com.hifs.app.system.service.ISystemAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemAuthUserServiceImpl implements ISystemAuthUserService {
    @Autowired
    private SystemAuthUserRepository userRepository;

    @Autowired
    private SystemAuthUserMapper userMapper;

    @Override
    public List<SystemAuthUserEntity> findAll() {
        return List.of();
    }

    @Override
    public List<SystemAuthUserEntity> findAll1() {
        return List.of();
    }

    @Override
    public SystemAuthUserEntity findById(Long id) {
        return null;
    }

    @Override
    public void createUser(SystemAuthUserEntity user) {

    }

    @Override
    public void updateUser(SystemAuthUserEntity user) {

    }

    @Override
    public void deleteUser(Long id) {

    }
}
