package com.learn.hibernate.base;

import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.utils.ClassUtils;
import com.learn.hibernate.utils.MyStringUtils;
import lombok.Data;
import org.hibernate.Session;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@Data
public class UpdateQuery<T> extends BaseQuery<T> {



    public UpdateQuery(MyQuery myQuery, Session session) {
        this.setSession(session);
        this.setMyQuery(myQuery);
        this.getMyQuery().setUpdateSql(new StringBuilder("update "+ super.getCurrentTableName()+ " set "));
    }


    public UpdateQuery set(PageData pageData) {
        StringBuilder sb = new StringBuilder();
        pageData.getMap().forEach(
                (k, v) -> {
                    sb.append(MyStringUtils.getSqlWord(k) + " = '" + v + "',");
                }
        );
        this.getMyQuery().getUpdateSql().append(sb.toString());
        return this;
    }

    public UpdateQuery set(String filed, Object value) {
        set(new PageData(filed,value));
        return this;
    }

    public UpdateQuery set(T t) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        var clz = this.getMyQuery().getAClass();
        if(!clz.equals(t.getClass())) {
            throw new RuntimeException("操作的数据与当前实例不符");
        }
        var map = ClassUtils.tToMap(t);
        PageData pageData = new PageData(map);
        set(pageData);
        return this;
    }







}
