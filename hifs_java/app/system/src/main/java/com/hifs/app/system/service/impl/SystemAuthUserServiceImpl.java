package com.hifs.app.system.service.impl;

import com.hifs.app.system.core.security.context.AuthenticationContextHolder;
import com.hifs.app.system.dao.mapper.SystemAuthUserMapper;
import com.hifs.app.system.dao.repository.SystemAuthUserRepository;
import com.hifs.app.system.domain.entity.SystemAuthUserEntity;
import com.hifs.app.system.service.ISystemAuthUserService;
import com.hifs.hicore.exception.ServiceException;
import com.hifs.hicore.utils.SecurityUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "loginAttempts")
public class SystemAuthUserServiceImpl implements ISystemAuthUserService, UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(SystemAuthUserServiceImpl.class);
    private static final int MAX_FAIL_COUNT = 10;
    private static final long LOCK_DURATION = 60 * 24; // 锁定时间（分钟）

    @Autowired
    private SystemAuthUserRepository systemAuthUserRepository;

    @Autowired
    private SystemAuthUserMapper systemAuthUserMapper;

    // 查询所有用户
    public List<SystemAuthUserEntity> findAll() {
        return systemAuthUserMapper.selectList(null);
    }


    // 根据ID查询用户
    public Optional<SystemAuthUserEntity> findById(Long id) {
        return systemAuthUserRepository.findById(id);
    }


    // 插入新用户
    public void addUser(SystemAuthUserEntity user) {
        systemAuthUserMapper.insert(user);
    }

    // 更新用户信息
    public void updateUser(SystemAuthUserEntity user) {
        systemAuthUserMapper.updateById(user);
    }

    // 删除用户
    public void deleteUser(Long id) {
        systemAuthUserMapper.deleteById(id);
    }

    //用于security的UserDetailsService实现
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemAuthUserEntity user = systemAuthUserRepository.findByUsername(username);
        if (user == null) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("用户不存在");
        }
        if (user.getStatus() == 0) {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("用户已被停用");
        }
        // 从context线程中获取密码，验证密码
        validatePasswd(user);

        // 构建UserDetails对象并返回
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(user.getStatus() == 0)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }

    // 验证密码是否正确
    public void validatePasswd(SystemAuthUserEntity user) {
        String username = user.getUsername();
        LoginAttempt loginAttempt = getLoginAttempt(username);

        // 检查是否在锁定期
        if (loginAttempt != null && loginAttempt.isLocked()) {
            if (loginAttempt.getLockTime().plusMinutes(LOCK_DURATION).isAfter(LocalDateTime.now())) {
                log.info("登录用户：{} 已被锁定", username);
                throw new ServiceException("账户已被锁定，请稍后再试");
            } else {
                // 锁定时间已过，清除记录
                clearLoginAttempt(username);
                loginAttempt = null;
            }
        }

        // 获取context中的密码
        String password = AuthenticationContextHolder.getContext().getCredentials().toString();

        // 验证密码
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            loginAttempt = updateFailedAttempt(username, loginAttempt);

            // 检查是否达到锁定阈值
            if (loginAttempt.getFailCount() >= MAX_FAIL_COUNT) {
                loginAttempt.setLocked(true);
                loginAttempt.setLockTime(LocalDateTime.now());
                updateLoginAttempt(username, loginAttempt);

                log.info("登录用户：{} 因连续失败次数达到{}次，账户已被锁定{}分钟",
                        username, MAX_FAIL_COUNT, LOCK_DURATION);
                throw new ServiceException("账户已被锁定，请" + LOCK_DURATION + "分钟后再试");
            }

            log.info("登录用户：{} 密码错误，失败次数：{}", username, loginAttempt.getFailCount());
            throw new ServiceException("密码错误，还剩" + (MAX_FAIL_COUNT - loginAttempt.getFailCount()) + "次机会");
        }

        // 登录成功，清除失败记录
        clearLoginAttempt(username);
        log.info("登录用户：{} 登录成功", username);
    }

    @Cacheable(key = "#id")
    public LoginAttempt getLoginAttempt(String username) {
        return null; // 首次调用返回null
    }

    @CachePut(key = "#id")
    public LoginAttempt updateLoginAttempt(String username, LoginAttempt loginAttempt) {
        return loginAttempt;
    }

    @CacheEvict(key = "#id")
    public void clearLoginAttempt(String username) {
        // 方法体可以为空，注解会处理缓存的清除
    }

    private LoginAttempt updateFailedAttempt(String username, LoginAttempt loginAttempt) {
        if (loginAttempt == null) {
            loginAttempt = new LoginAttempt();
            loginAttempt.setFailCount(1);
        } else {
            loginAttempt.setFailCount(loginAttempt.getFailCount() + 1);
        }
        return updateLoginAttempt(username, loginAttempt);
    }
}

@Data
class LoginAttempt implements Serializable {
    private int failCount;
    private boolean locked;
    private LocalDateTime lockTime;
}
