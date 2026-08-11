package com.pzx.knowledge.controller;

import com.pzx.knowledge.common.exception.BusinessException;
import com.pzx.knowledge.common.result.Result;
import com.pzx.knowledge.common.result.ResultCode;
import com.pzx.knowledge.dto.LoginDTO;
import com.pzx.knowledge.dto.RegisterDTO;
import com.pzx.knowledge.service.UserService;
import com.pzx.knowledge.vo.LoginResultVO;
import com.pzx.knowledge.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<LoginResultVO> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.ok(userService.register(dto));
    }


    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginResultVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(userService.login(dto));
    }


    @GetMapping("/me")
    public Result<UserVO> me(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth==null || auth.getPrincipal()==null){
            log.error("从线程中无法找到用户ID");
            throw  new BusinessException(ResultCode.THREAD_NOT_FOUND_ID);
        }
        Long userId =(Long)auth.getPrincipal();
        return Result.ok(userService.getCurrentUser(userId));
    }
}