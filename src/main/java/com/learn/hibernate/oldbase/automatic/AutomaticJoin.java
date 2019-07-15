package com.learn.hibernate.oldbase.automatic;

import com.learn.hibernate.base.BaseQuery;
import com.learn.hibernate.base.MyQuery;
import com.learn.hibernate.base.WhereQuery;
import lombok.Data;

import javax.persistence.criteria.JoinType;
import java.util.HashMap;
import java.util.Random;

/**
 * join
 * email leeoohoo@gmail.com
 * hibernate
 * Created by lee on 2019-07-09.
 */
@Data
public class AutomaticJoin extends BaseQuery {



    private String tableName;

    public AutomaticJoin(String tableName, JoinType joinType, MyQuery myQuery) {
        this.setMyQuery(myQuery);
        this.tableName = tableName;
        this.getMyQuery().setJoinEntityNameAlias(new HashMap<>());
        String aliasName = tableName + new Random().nextInt(1000);//join的别名表名
        this.getMyQuery().getJoinEntityNameAlias().put(tableName, aliasName);
        String entityName = this.getMyQuery().getEntityName();//当前查询表的表明
        String entityAliasName = this.getMyQuery().getEntityNameAlias();//当前查询表的别名
        this.getMyQuery()
                .setJoinSql(
                        new StringBuilder(
                                " "
                                        + joinType
                                        + " join "
                                        + tableName + " "
                                        + aliasName
                                        + " "
                                        + " on "
                                        + entityAliasName + "." + entityName + "Id = "
                                        + aliasName + ".id "
                        )
                );
    }

    public AutomaticJoin followUp(String tableName, JoinType joinType) {
        var aliasName = tableName + new Random().nextInt(1000);
        this.getMyQuery().getJoinEntityNameAlias().put(tableName, aliasName);
        this.getMyQuery().getJoinSql().append(
                " "
                        + joinType
                        + " join "
                        + tableName + " "
                        + aliasName
                        + " "
                        + " on "
                        + this.getMyQuery().getJoinEntityNameAlias().get(this.tableName) + "." + this.tableName + "Id = "
                        + aliasName + ".id "

        );
        this.tableName = tableName;
        return this;
    }

    public AutomaticJoin join(String tableName, JoinType joinType) {
        var aliasName = tableName + new Random().nextInt(1000);
        this.getMyQuery().getJoinEntityNameAlias().put(tableName, aliasName);
        String entityName = this.getMyQuery().getEntityName();//当前查询表的表明
        String entityAliasName = this.getMyQuery().getEntityNameAlias();//当前查询表的别名
        this.getMyQuery().getJoinSql().append(
                " "
                        + joinType
                        + " join "
                        + tableName + " "
                        + aliasName
                        + " "
                        + " on "
                        + entityAliasName + "." + entityName + "Id = "
                        + aliasName + ".id "

        );
        this.tableName = tableName;
        return this;
    }

    public WhereQuery where() {
        return new WhereQuery(this.getMyQuery(),this.getSession());
    }

}
