package com.learn.hibernate.curd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 给前台返回的结果
 * email leeoohoo@gmail.com
 * system
 * Created by lee on 2019/9/28.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private String message;

}
