package com.learn.hibernate.base;

import lombok.Data;
import org.springframework.context.annotation.Scope;

@Data
@Scope(scopeName = "prototype")
public class MyQuery {

    private Class aClass;

    private StringBuilder updateSql;

    private StringBuilder whereSql;

}
