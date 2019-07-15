package com.learn.hibernate.base;

import lombok.Data;
import org.springframework.context.annotation.Scope;

import java.util.Map;

@Data
@Scope(scopeName = "prototype")
public class MyQuery {

    private Class aClass;

    private StringBuilder updateSql;

    private StringBuilder whereSql;

    private StringBuilder selectSql;

    private StringBuilder joinSql;

    private String entityName;

    private String entityNameAlias;

    private Map<String,String> joinEntityNameAlias;

    private boolean isT = true;

}
