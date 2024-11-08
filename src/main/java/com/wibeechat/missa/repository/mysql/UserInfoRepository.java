package com.wibeechat.missa.repository.mysql;

import com.wibeechat.missa.entity.mysql.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    // 기본적인 CRUD 메서드들이 자동으로 제공됩니다
    Optional<UserInfo> findByUserId(String userId);
}