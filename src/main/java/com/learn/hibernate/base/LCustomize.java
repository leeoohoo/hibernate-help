package com.learn.hibernate.base;

import lombok.Data;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.query.QueryProducer;

@Data
public class LCustomize {

    private BaseDao baseDao;

    public LCustomize(BaseDao baseDao) {
        this.baseDao = baseDao;
    }


    public LCustomizeSelect find(String sqlString) {
        return new LCustomizeSelect(this.baseDao,sqlString);
    }




}
