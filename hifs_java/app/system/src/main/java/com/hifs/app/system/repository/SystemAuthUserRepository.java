package com.hifs.app.system.repository;

import com.hifs.app.system.domain.entity.SystemAuthUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SystemAuthUserRepository extends JpaRepository<SystemAuthUserEntity, Long> {

}
