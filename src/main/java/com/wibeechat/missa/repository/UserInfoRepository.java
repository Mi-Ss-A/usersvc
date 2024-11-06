package com.wibeechat.missa.repository;

import com.wibeechat.missa.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<Long, UserInfo> {
    Optional<UserInfo> findByUserId(String userId);
}
