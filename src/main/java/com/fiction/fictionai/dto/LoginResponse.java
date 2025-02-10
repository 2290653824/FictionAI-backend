package com.fiction.fictionai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhengjian
 * @date 2025-02-10 22:43
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse implements Serializable {
    private String id;
    private String email;
    private String createdAt;
    private String updatedAt;
}
