package com.hifs.app.system.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hifs.app.system.domain.entity.SystemAuthUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemAuthUserMapper extends BaseMapper<SystemAuthUserEntity> {
    List<SystemAuthUserEntity> findAll();
}
