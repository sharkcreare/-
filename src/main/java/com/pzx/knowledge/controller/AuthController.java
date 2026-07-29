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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterDTO dto) {

        return Result.ok(userService.register(dto));

    }
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginResultVO> login(@Valid @RequestBody LoginDTO dto) {

        return Result.ok(userService.login(dto));

    }


    @GetMapping("/me")
    public Result<UserVO> me(){
        Long userId =(Long) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(userId)){
            log.error("从线程中无法找到用户ID");
            throw  new BusinessException(ResultCode.THREAD_NOT_FUND_ID);
        }
        return Result.ok(userService.getCurrentUser(userId));
    }
}