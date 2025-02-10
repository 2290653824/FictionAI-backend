package com.fiction.fictionai.controller.auth;

import com.fiction.fictionai.dao.UserMapper;
import com.fiction.fictionai.dto.LoginRequest;
import com.fiction.fictionai.dto.LoginResponse;
import com.fiction.fictionai.dto.RegisterRequest;
import com.fiction.fictionai.dto.RegisterResponse;
import com.fiction.fictionai.entity.User;
import com.fiction.fictionai.encoder.BCryptPasswordEncoder;
import com.fiction.fictionai.service.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserMapper userMapper; // Your repository to fetch user data

    @Autowired
    private JwtTokenService jwtTokenService; // Service to create JWT tokens

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Password encoder

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            if (email == null || password == null) {
                return ResponseEntity.badRequest().body("请输入邮箱和密码");
            }

            User user = userMapper.getByEmail(email);

            if (user == null) {
                return ResponseEntity.status(401).body("账号或密码错误");
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(401).body("账号或密码错误");
            }

            // Create JWT token
            String token = jwtTokenService.createToken(user.getId());

            // Set the token in the response cookie
            Cookie cookie = new Cookie("auth-token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // Set based on production environment
            cookie.setPath("/");
            cookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
            response.addCookie(cookie);

            return ResponseEntity.ok(new LoginResponse(user.getId(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("登录失败，请稍后重试");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        try {
            // Delete the authentication cookie
            Cookie cookie = new Cookie("auth-token", "");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(0); // Delete the cookie immediately
            response.addCookie(cookie);

            return ResponseEntity.ok("注销成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("注销失败，请重试");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        try {
            String email = registerRequest.getEmail();
            String password = registerRequest.getPassword();

            if (email == null || password == null) {
                return ResponseEntity.badRequest().body("请输入邮箱和密码");
            }

            // Validate email format
            if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                return ResponseEntity.badRequest().body("请输入有效的邮箱地址");
            }

            // Validate password length
            if (password.length() < 6) {
                return ResponseEntity.badRequest().body("密码长度至少为6位");
            }

            // Check if the email already exists
            User existedUser = userMapper.getByEmail(email);

            if (existedUser != null) {
                return ResponseEntity.badRequest().body("该邮箱已被注册");
            }

            // Encrypt the password
            String hashedPassword = passwordEncoder.encode(password);

            // Create the user
            User user = new User();
            user.setEmail(email);
            user.setPassword(hashedPassword);
            int res = userMapper.save(user);
            if(res==0){
                throw new Exception("保存账户信息失败");
            }
            return ResponseEntity.ok(new RegisterResponse(user.getId(), user.getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("注册失败，请稍后重试");
        }
    }
}
