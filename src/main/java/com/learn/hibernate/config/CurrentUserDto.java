package com.learn.hibernate.config;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import lombok.Data;

import java.util.List;

@Data
public class CurrentUserDto {
    private Integer id;

    private String username;

    private String password;

    private Integer administratorLevel;

    private Integer employeeId;

    private String employeeName;

    private Integer organizationId;

    private String organizationName;

    @Ignore
    private String tokenKey;

    private Integer isAccountNonLocked;

    private Integer isEnabled;
}
