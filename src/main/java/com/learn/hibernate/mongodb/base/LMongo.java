package com.learn.hibernate.mongodb.base;

import com.learn.hibernate.domian.PageData;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.result.DeleteResult;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * mongodb 的工具类
 * leeoohoo@gmail.com
 * @param <T>
 */
@Component
@Data
public class LMongo<T> {

    @Autowired
    private MongoQuery<T> mq;

    public boolean save(T t) {
        this.mq.getMongoTemplate().save(t);
        return true;
    }

    public Long delete(T t) {
        var result =  this.mq.getMongoTemplate().remove(t);
        return result.getDeletedCount();
    }

    public SelectQuery find(Class<T> tClass) {
        this.mq.setTClass(tClass);
        return new SelectQuery(this.mq);
    }

    public LMongo delete(Class<T> tClass) {
        this.mq.setTClass(tClass);
        return this;
    }








}
