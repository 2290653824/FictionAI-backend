package com.fiction.fictionai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * @author zhengjian
 * @date 2025-02-10 22:50
 */
@Service
public class JwtTokenService {

    public String createToken(String userId) {

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();

        // 创建 token 格式：userId:timestamp
        String tokenString = userId + ":" + timestamp;

        // 将字符串进行 Base64 编码
        String token = Base64.getEncoder().encodeToString(tokenString.getBytes());

        return token;

    }
}
