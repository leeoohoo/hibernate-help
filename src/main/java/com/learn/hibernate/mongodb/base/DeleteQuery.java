package com.learn.hibernate.mongodb.base;

import com.learn.hibernate.domian.PageData;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;

import java.util.List;

public class DeleteQuery<T> {

    protected MongoQuery<T> mq;
    
    public DeleteQuery (MongoQuery mongoQuery) {
        this.mq = mongoQuery;
    }


    public WhereQuery where() {
        return new WhereQuery(this.mq);
    }

    public WhereQuery where(PageData pageData) {
        this.mq.setPageData(pageData);
        return new WhereQuery(this.mq);
    }

}
