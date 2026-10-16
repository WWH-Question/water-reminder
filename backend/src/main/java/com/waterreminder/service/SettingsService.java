package com.waterreminder.service;

import com.waterreminder.dto.SettingsDTO;
import com.waterreminder.entity.mysql.UserInfo;
import com.waterreminder.repository.mysql.UserInfoRepository;
import com.waterreminder.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingsService {

    private UserInfoRepository userInfoRepository;
    private CurrentUserService currentUserService;

    // CGLIB 事务代理需要无参构造
    public SettingsService() {}

    @Autowired
    public SettingsService(UserInfoRepository userInfoRepository,
                           CurrentUserService currentUserService) {
        this.userInfoRepository = userInfoRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional("mysqlTransactionManager")
    public UserInfo getSettings() {
        return currentUserService.getCurrentUser();
    }

    @Transactional("mysqlTransactionManager")
    public SettingsDTO getSettingsDTO() {
        UserInfo u = getSettings();
        return toDTO(u);
    }

    @Transactional("mysqlTransactionManager")
    public SettingsDTO updateSettings(SettingsDTO dto) {
        UserInfo u = currentUserService.getCurrentUser();
        if (dto.getHeight() != null) u.setHeight(dto.getHeight());
        if (dto.getWeight() != null) u.setWeight(dto.getWeight());
        if (dto.getCoefficient() != null) u.setCoefficient(dto.getCoefficient());
        if (dto.getWorkStart() != null) u.setWorkStart(dto.getWorkStart());
        if (dto.getWorkEnd() != null) u.setWorkEnd(dto.getWorkEnd());
        if (dto.getSleepTime() != null) u.setSleepTime(dto.getSleepTime());
        userInfoRepository.save(u);
        return toDTO(u);
    }

    private SettingsDTO toDTO(UserInfo u) {
        return SettingsDTO.builder()
                .height(u.getHeight())
                .weight(u.getWeight())
                .coefficient(u.getCoefficient())
                .workStart(u.getWorkStart())
                .workEnd(u.getWorkEnd())
                .sleepTime(u.getSleepTime())
                .build();
    }
}
