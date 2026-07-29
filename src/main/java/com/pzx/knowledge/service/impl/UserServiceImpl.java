package com.pzx.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.pzx.knowledge.common.exception.BusinessException;
import com.pzx.knowledge.common.result.ResultCode;
import com.pzx.knowledge.dto.LoginDTO;
import com.pzx.knowledge.dto.RegisterDTO;
import com.pzx.knowledge.entity.User;
import com.pzx.knowledge.mapper.UserMapper;
import com.pzx.knowledge.service.UserService;
import com.pzx.knowledge.utils.JwtUtils;
import com.pzx.knowledge.vo.LoginResultVO;
import com.pzx.knowledge.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserVO register(RegisterDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (this.count(wrapper) > 0) {
            log.warn("register failed - username exists: {}", dto.getUsername());
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        this.save(user);
        return toVO(user);
    }

    @Override
    public LoginResultVO login(LoginDTO dto) {
        User user= lambdaQuery()
                .eq(User::getUsername,dto.getUsername())
                .one();

        if(Objects.isNull(user)){
            log.error("用户查询不到");
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            log.error("用户密码错误");
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        String token =jwtUtils.generateToken(user.getId(),user.getUsername());

        LoginResultVO result = new LoginResultVO();
        result.setToken(token);
        result.setUser(toVO(user));
        return result;
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            log.warn("get current user failed - not found: userId={}", userId);
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return toVO(user);
    }

    private UserVO toVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}