package com.waterreminder.service;

import com.waterreminder.dto.AdminUserDTO;
import com.waterreminder.entity.mysql.UserInfo;
import com.waterreminder.repository.mysql.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private UserInfoRepository userInfoRepository;

    // CGLIB 事务代理需要无参构造
    public AdminService() {}

    @Autowired
    public AdminService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Transactional("mysqlTransactionManager")
    public List<AdminUserDTO> getAllUsers() {
        return userInfoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional("mysqlTransactionManager")
    public AdminUserDTO updateUserSettings(Long userId, AdminUserDTO dto) {
        UserInfo u = userInfoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        if (dto.getHeight() != null) u.setHeight(dto.getHeight());
        if (dto.getWeight() != null) u.setWeight(dto.getWeight());
        if (dto.getCoefficient() != null) u.setCoefficient(dto.getCoefficient());
        if (dto.getWorkStart() != null) u.setWorkStart(dto.getWorkStart());
        if (dto.getWorkEnd() != null) u.setWorkEnd(dto.getWorkEnd());
        if (dto.getSleepTime() != null) u.setSleepTime(dto.getSleepTime());
        userInfoRepository.save(u);
        return toDTO(u);
    }

    private AdminUserDTO toDTO(UserInfo u) {
        return AdminUserDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .height(u.getHeight())
                .weight(u.getWeight())
                .coefficient(u.getCoefficient())
                .workStart(u.getWorkStart())
                .workEnd(u.getWorkEnd())
                .sleepTime(u.getSleepTime())
                .build();
    }
}
