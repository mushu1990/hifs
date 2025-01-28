package com.hifs.app.system.dao.repository;

import com.hifs.app.system.domain.entity.SystemAuthUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SystemAuthUserRepository extends JpaRepository<SystemAuthUserEntity, Long> {
    // 根据用户名查询用户
    SystemAuthUserEntity findByUsername(String username);
}
