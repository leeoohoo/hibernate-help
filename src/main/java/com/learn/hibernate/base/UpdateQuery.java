package com.learn.hibernate.base;

import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.utils.ClassUtils;
import lombok.Data;
import org.hibernate.Session;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@Data
public class UpdateQuery<T> extends BaseQuery<T> {



    public UpdateQuery(MyQuery myQuery, Session session) {
        this.session = session;
        this.myQuery = myQuery;
        this.myQuery.setUpdateSql(new StringBuilder("update "+ super.getCurrentTableName()+ " set "));
    }


    public UpdateQuery set(PageData pageData) {
        StringBuilder sb = new StringBuilder();
        pageData.getMap().forEach(
                (k, v) -> {
                    sb.append(k + " = '" + v + "',");
                }
        );
        this.myQuery.getUpdateSql().append(sb.toString());
        return this;
    }

    public UpdateQuery set(String filed, Object value) {
        StringBuilder sb = new StringBuilder();
        this.myQuery.getUpdateSql().append(sb.toString());
        return this;
    }

    public UpdateQuery set(T t) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        var clz = this.myQuery.getAClass();
        if(!clz.equals(t.getClass())) {
            throw new RuntimeException("操作的数据与当前实例不符");
        }
        var map = ClassUtils.tToMap(t);
        PageData pageData = new PageData(map);
        set(pageData);
        return this;
    }







}
