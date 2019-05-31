package com.learn.hibernate.base;

import lombok.Data;

@Data
public class MyQuery {

    private Class aClass;

    private StringBuilder updateSql;

    private StringBuilder whereSql;

}
