package com.svnlan.jwt.controller;

import com.svnlan.common.Result;
import com.svnlan.jwt.dto.AutoLoginDto;
import com.svnlan.jwt.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录认证相关
 *
 */
@Slf4j
@RestController
@RequestMapping("api/disk/cube")
public class AuthController {
    @Resource
    private AuthService authService;

    @CrossOrigin
    @PostMapping("autoLogin")
    public Result autoLogin(@RequestBody @Validated AutoLoginDto dto) {
       return Result.returnSuccess(authService.autoLogin(dto));
    }




}



















