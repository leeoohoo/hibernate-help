package com.learn.hibernate.domian;

import lombok.Data;

import java.math.BigInteger;

@Data
public class EmployeeDto {

    private Long id;

    private String name;

    private String organName;

    private String dempatmentName;
}