package com.learn.hibernate.oldbase.automatic;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.base.BaseQuery;
import lombok.Data;

import javax.persistence.criteria.JoinType;
import java.util.Random;


/**
 * TODO
 * email leeoohoo@gmail.com
 * hibernate
 * Created by lee on 2019-07-07.
 */
@Data
public class LCustomizeSelectAutomatic extends BaseQuery {

    private BaseDao baseDao;

    private Class tClass;

    private Class dtoClass;


    public LCustomizeSelectAutomatic(BaseDao baseDao) {
        this.baseDao = baseDao;
        this.tClass = baseDao.getTClass();
        this.dtoClass = baseDao.getDtoClass();
        this.getMyQuery().setSelectSql(new StringBuilder("select "));
        var entityName = tClass.getName().substring(tClass.getName().lastIndexOf('.') + 1);
        this.getMyQuery().setEntityName(entityName);

        this.getMyQuery().setEntityNameAlias(entityName + "_" + new Random().nextInt(1000));

    }



    public AutomaticJoin join(String tableName, JoinType joinType) {
        return new AutomaticJoin(tableName, joinType, this.getMyQuery());
    }




}
