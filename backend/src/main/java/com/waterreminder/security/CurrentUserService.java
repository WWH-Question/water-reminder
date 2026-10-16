package com.waterreminder.security;

import com.waterreminder.entity.mysql.UserInfo;
import com.waterreminder.repository.mysql.UserInfoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserInfoRepository userInfoRepository;

    public CurrentUserService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public UserInfo getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserInfo)) {
            throw new RuntimeException("User not authenticated");
        }
        UserInfo user = (UserInfo) auth.getPrincipal();
        return userInfoRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
