package com.wibeechat.missa.repository.mysql;

import com.wibeechat.missa.entity.mysql.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    Optional<UserInfo> findByUserId(String userId);
}
