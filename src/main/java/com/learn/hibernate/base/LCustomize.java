package com.learn.hibernate.base;

import com.learn.hibernate.utils.SpringUtil;
import lombok.Data;

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
