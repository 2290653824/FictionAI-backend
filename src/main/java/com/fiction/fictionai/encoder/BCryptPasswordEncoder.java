package com.fiction.fictionai.encoder;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * @author zhengjian
 * @date 2025-02-10 22:47
 */
@Service
public class BCryptPasswordEncoder {

    public boolean matches(String password, String storedPassword) {
        return BCrypt.checkpw(password, storedPassword);
    }

    public String encode(String password) {
       return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }
}
