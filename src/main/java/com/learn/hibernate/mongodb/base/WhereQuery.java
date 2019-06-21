package com.learn.hibernate.mongodb.base;

import com.learn.hibernate.domian.PageData;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.query.BasicQuery;

import java.util.List;
import java.util.regex.Pattern;

public class WhereQuery<T>{
    private MongoQuery<T> mq;
    public WhereQuery (MongoQuery mongoQuery) {
        this.mq = mongoQuery;
    }




    public Long delete() {
        this.where();
        if(null == this.mq.getTClass() || null == this.mq.getQueryBuilder() || null == this.mq.getQuery()) {
            throw new RuntimeException("没有删除条件");
        }
        DeleteResult remove = this.mq.getMongoTemplate().remove(this.mq.getQuery(), this.mq.getTClass());
        return remove.getDeletedCount();
    }



    public List<T> findList() {
        this.where();
        initSelect();
        this.mq.setQuery(new BasicQuery(this.mq.getQueryBuilder().get().toString(), this.mq.getFieldsObject().toJson()));
        return mq.getMongoTemplate().find(this.mq.getQuery(), this.mq.getTClass());
    }

    public Page<T> findPage() {
        this.where();
        if (null == this.mq.getPageable()) {
            throw new RuntimeException("请先设置分页");
        }
        var list = findList();
        long total = this.mq.getMongoTemplate().count(this.mq.getQuery(), this.mq.getTClass());
        Page<T> page = new PageImpl<>(list, this.mq.getPageable(), total);
        return page;
    }

    public T findOne() {
        this.where();
        this.initSelect();
        this.mq.setQuery(new BasicQuery(this.mq.getQueryBuilder().get().toString(), this.mq.getFieldsObject().toJson()));
        return this.mq.getMongoTemplate().findOne(this.mq.getQuery(), this.mq.getTClass());
    }

    private void initSelect() {
        if (null == this.mq.getFieldsObject()) {
            this.mq.setFieldsObject(new BasicDBObject());
        }
    }


    public WhereQuery or(BasicDBObject... basicDBObjects) {
        this.mq.getQueryBuilder().or(basicDBObjects);
        return this;
    }


    public WhereQuery eq(String key, Object o) {
        this.mq.getQueryBuilder().and(key).is(o);
        return this;
    }

    public WhereQuery like(String key, String o) {
        Pattern pattern = Pattern.compile("^.*" + o + ".*$", Pattern.CASE_INSENSITIVE);
        this.mq.getQueryBuilder().and(key).regex(pattern);
        return this;
    }

    public WhereQuery gt(String key, Object o) {
        this.mq.getQueryBuilder().and(key).greaterThan(o);
        return this;
    }

    public WhereQuery ge(String key, Object o) {
        this.mq.getQueryBuilder().and(key).greaterThanEquals(o);
        return this;
    }

    public WhereQuery lt(String key, Object o) {
        this.mq.getQueryBuilder().and(key).lessThan(o);
        return this;
    }

    public WhereQuery le(String key, Object o) {
        this.mq.getQueryBuilder().and(key).is(o);
        return this;
    }

    public WhereQuery in(String key, List o) {
        this.mq.getQueryBuilder().and(key).in(o);
        return this;
    }

    public WhereQuery notIn(String key, List o) {
        this.mq.getQueryBuilder().and(key).notIn(o);
        return this;
    }

    private WhereQuery where() {
        if (this.mq.getPageData() == null) {
            return this;
        }
        this.mq.getPageData().getMap().forEach(
                (k, v) -> {
                    var stes = k.split("_");
                    if (stes.length > 1) {
                        switch (stes[1]) {
                            case "like":
                                var value = v.toString();
                                if (value != null && !"".equals(value.trim())) {
                                    this.like(stes[0], value);
                                }
                                break;
                            case "eq":
                                if (v != null) {
                                    this.eq(stes[0],v);
                                }
                                break;
                            case "gt":
                                if (v != null) {
                                    this.gt(stes[0],v);
                                }
                                break;
                            case "ge":
                                if (v != null) {
                                    this.ge(stes[0],v);
                                }
                                break;
                            case "le":
                                if (v != null) {
                                    this.le(stes[0],v);
                                }
                                break;
                            case "lt":
                                if (v != null) {
                                    this.lt(stes[0],v);
                                }
                                break;
                            case "in":
                                if (v != null) {
                                    var list = (List) v;
                                    if (list.size() > 0) {
                                        this.in(stes[0],list);
                                    }
                                }

                                break;
                            case "notIn":
                                if (v != null) {
                                    var list = (List) v;
                                    if (list.size() > 0) {
                                        this.notIn(stes[0],list);
                                    }
                                }
                                break;

                        }
                    }

                }
        );
        return this;
    }

}
