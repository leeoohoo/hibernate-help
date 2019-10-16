package com.learn.hibernate.curd;

import lombok.Data;

/**
 * 当前登录用户的容器
 * email leeoohoo@gmail.com
 * system
 * Created by lee on 2019/9/28.
 */
@Data
public class CurrentUserDto {

    private Integer id;

    private String employeeName;

}
