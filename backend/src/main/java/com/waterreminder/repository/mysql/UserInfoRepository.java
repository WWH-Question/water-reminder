package com.waterreminder.repository.mysql;

import com.waterreminder.entity.mysql.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByUsername(String username);

    boolean existsByUsername(String username);
}
