package com.pzx.knowledge.service;
import com.baomidou.mybatisplus.spring.service.IService;
import com.pzx.knowledge.dto.LoginDTO;
import com.pzx.knowledge.dto.RegisterDTO;
import com.pzx.knowledge.entity.User;
import com.pzx.knowledge.vo.LoginResultVO;
import com.pzx.knowledge.vo.UserVO;
public interface UserService extends IService<User> {
    LoginResultVO register(RegisterDTO dto);
    LoginResultVO login(LoginDTO dto);
    UserVO getCurrentUser(Long userId);
}

