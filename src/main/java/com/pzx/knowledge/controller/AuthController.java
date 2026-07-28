package com.pzx.knowledge.controller;

import com.pzx.knowledge.common.result.Result;
import com.pzx.knowledge.dto.LoginDTO;
import com.pzx.knowledge.dto.RegisterDTO;
import com.pzx.knowledge.service.UserService;
import com.pzx.knowledge.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    // 构造器注入（推荐方式，不需要 @Autowired）
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.ok(userService.register(dto));
    }

    @PostMapping("/login")
    public Result<UserVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(userService.login(dto));
    }
}