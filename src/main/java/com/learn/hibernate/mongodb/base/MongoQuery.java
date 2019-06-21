package com.learn.hibernate.mongodb.base;

import com.learn.hibernate.domian.PageData;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@Data
public class MongoQuery<T> {
    @Autowired
    private MongoTemplate mongoTemplate;

    private QueryBuilder queryBuilder;

    private Pageable pageable;

    private Sort sort;

    private Query query;

    private BasicDBObject fieldsObject;

    private Class<T> tClass;

    private PageData pageData;
}
