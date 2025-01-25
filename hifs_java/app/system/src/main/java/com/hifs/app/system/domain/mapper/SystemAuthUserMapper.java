package com.hifs.app.system.domain.mapper;

import com.hifs.app.system.domain.entity.SystemAuthUserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SystemAuthUserMapper {
    @Select("SELECT * FROM system_auth_user")
    List<SystemAuthUserEntity> findAll();

    @Select("SELECT * FROM system_auth_user WHERE id = #{id}")
    SystemAuthUserEntity findById(Long id);

    @Insert("INSERT INTO system_auth_user (username, password) VALUES (#{name}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(SystemAuthUserEntity user);

    @Update("UPDATE system_auth_user SET username = #{name}, password = #{email} WHERE id = #{id}")
    void update(SystemAuthUserEntity user);

    @Delete("DELETE FROM system_auth_user WHERE id = #{id}")
    void delete(Long id);
}
