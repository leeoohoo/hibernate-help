package com.learn.hibernate.base;

import com.learn.hibernate.domian.PageData;
import lombok.Data;
import org.hibernate.sql.JoinType;
import org.springframework.context.annotation.Scope;


@Data
@Scope(scopeName = "prototype")
@SuppressWarnings({"unused", "unchecked", "rawtypes", "null", "hiding"})
public class LJoin {

    private LSelect lSelect;

    private String tableName;


    public LJoin(String tableName, JoinType joinType, LSelect lSelect) {
        this.lSelect = lSelect;
        var a = this.lSelect.getBaseDao();
        this.tableName = tableName;
        var b = this.lSelect.getBaseDao().getJoinTypeMap();
        this.lSelect.getBaseDao().getJoinTypeMap().put(tableName, joinType);
        this.lSelect.getBaseDao().getJoinTableName().add(tableName);
    }

    public LJoin on(PageData pageData) {
        this.lSelect.getBaseDao().getStringSimpleExpressionMap().put(this.tableName, this.lSelect.getBaseDao().initWhere(pageData));
        return this;
    }

    public LJoin followUp(String tableName, JoinType joinType) {
        this.lSelect.getBaseDao().getJoinTableName().add(this.tableName+"."+tableName);
        this.lSelect.getBaseDao().getJoinTypeMap().put(this.tableName+"."+tableName, joinType);
        this.tableName = tableName;
        return this;
    }

    public LJoin justJoin(String tableName, JoinType joinType) {
        this.tableName = tableName;
        this.lSelect.getBaseDao().getJoinTypeMap().put(tableName, joinType);
        this.lSelect.getBaseDao().getJoinTableName().add(tableName);
        return this;
    }


    public LSelect fetch() {
        return this.lSelect;
    }
}
