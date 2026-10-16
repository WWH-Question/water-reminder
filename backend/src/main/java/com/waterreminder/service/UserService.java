package com.waterreminder.service;

import com.waterreminder.dto.AuthResponse;
import com.waterreminder.dto.LoginRequest;
import com.waterreminder.dto.RegisterRequest;
import com.waterreminder.entity.mysql.UserInfo;
import com.waterreminder.repository.mysql.UserInfoRepository;
import com.waterreminder.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserInfoRepository userInfoRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional("mysqlTransactionManager")
    public AuthResponse register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("密码至少6位");
        }
        if (userInfoRepository.existsByUsername(request.getUsername().trim())) {
            throw new IllegalArgumentException("用户名已存在");
        }

        UserInfo user = UserInfo.builder()
                .username(request.getUsername().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
        user = userInfoRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }

        UserInfo user = userInfoRepository.findByUsername(request.getUsername().trim())
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        String token = jwtService.generateToken(user.getUsername());
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .build();
    }
}
