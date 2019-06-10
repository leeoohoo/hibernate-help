package com.learn.hibernate.base;

import lombok.Data;

@Data
public class LDelete {

    private  BaseDao baseDao;



    public LDelete(BaseDao baseDao) {
        this.baseDao = baseDao;
    }


    public WhereQuery where() {
        return this.baseDao.getBaseQuery().where(this);
    }


    public Integer execution(WhereQuery whereQuery) {
        var sql = whereQuery.toSql();
        return this.baseDao.delete(sql);
    }



}
