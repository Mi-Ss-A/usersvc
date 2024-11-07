package com.wibeechat.missa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    @Id
    @Column(name = "user_no", length = 100, nullable = false)
    private String userNo;

    @Column(name = "user_id", length = 20, nullable = false)
    private String userId;

    @Column(name = "user_pw", length = 100, nullable = false)
    private String userPw;

    @Column(name = "user_phone_number", length = 15, nullable = false)
    private String userPhoneNumber;

    @Column(name = "user_name", length = 20, nullable = false)
    private String userName;

    @Column(name = "user_email", length = 100, nullable = false)
    private String userEmail;

    @Column(name = "user_date_of_birth", nullable = false)
    private LocalDate userDateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", length = 1, nullable = false)
    private Gender userGender;

    @Column(name = "user_address", length = 200, nullable = false)
    private String userAddress;

    @Column(name = "user_registration_date")
    private LocalDateTime userRegistrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", length = 1)
    private UserStatus userStatus;

    @Column(name = "user_type", length = 5, nullable = false)
    private String userType;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL)
    private List<BankAccountInfo> bankAccounts = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addBankAccount(BankAccountInfo bankAccount) {
        this.bankAccounts.add(bankAccount);
        bankAccount.setUserInfo(this);
    }

    @PrePersist
    public void prePersist() {
        if (this.userStatus == null) {
            this.userStatus = UserStatus.A;
        }
        if (this.userRegistrationDate == null) {
            this.userRegistrationDate = LocalDateTime.now();
        }
    }
}