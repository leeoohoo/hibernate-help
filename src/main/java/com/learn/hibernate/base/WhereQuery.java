package com.learn.hibernate.base;

import com.learn.hibernate.utils.MyStringUtils;
import lombok.Data;
import org.hibernate.Session;

import java.util.List;

@Data
public class WhereQuery extends BaseQuery {

    private LDelete lDelete;

    public WhereQuery(MyQuery myQuery, Session session) {
        this.setSession(session);
        this.setMyQuery(myQuery);
        this.getMyQuery().setWhereSql(new StringBuilder(" where 1 = 1"));
    }

    public WhereQuery(LDelete lDelete) {
        this.lDelete = lDelete;
        this.getMyQuery().setWhereSql(new StringBuilder(" where 1 = 1"));
    }

    public WhereQuery in(String field, List list) {
        if (null != list && list.size() > 0) {
            this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field)  + " in (");
            this.getMyQuery().getWhereSql().append(getInValue(list));
            this.getMyQuery().getWhereSql().append(")");
        }
        return this;
    }

    public WhereQuery in(String field, String selectSql) {
        this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field)  + " in (");
        this.getMyQuery().getWhereSql().append(selectSql);
        this.getMyQuery().getWhereSql().append(")");
        return this;
    }

    public WhereQuery notIn(String field, String selectSql) {
        this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field)  + " not in (");
        this.getMyQuery().getWhereSql().append(selectSql);
        this.getMyQuery().getWhereSql().append(")");
        return this;
    }

    public WhereQuery notIn(String field, List list) {
        if(null != list && list.size() > 0) {
            this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field)  + " not in(");
            this.getMyQuery().getWhereSql().append(getInValue(list));
            this.getMyQuery().getWhereSql().append(")");
        }
        return this;
    }

    public WhereQuery eq(String field, Object value) {
        this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field) + " = '");
        this.getMyQuery().getWhereSql().append(value + "'");
        return this;
    }

    public WhereQuery lt(String field, Number value) {
        this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field)  + " < ");
        this.getMyQuery().getWhereSql().append(value);
        return this;
    }


    public WhereQuery le(String field, Number value) {
        this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field)  + " <= ");
        this.getMyQuery().getWhereSql().append(value);
        return this;
    }

    public WhereQuery gt(String field, Number value) {
        this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field)  + " > ");
        this.getMyQuery().getWhereSql().append(value);
        return this;
    }

    public WhereQuery ge(String field, Number value) {
        this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field)  + " >= ");
        this.getMyQuery().getWhereSql().append(value);
        return this;
    }

    public WhereQuery like(String field, String value) {
        this.getMyQuery().getWhereSql().append(" and " + MyStringUtils.getSqlWord(field)  + " like '%");
        this.getMyQuery().getWhereSql().append(value + "%'");
        return this;
    }

    public WhereQuery orEq(String field, Object value) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " = '");
        this.getMyQuery().getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orLt(String field, Object value) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " < '");
        this.getMyQuery().getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orLe(String field, Object value) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " <= '");
        this.getMyQuery().getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orGt(String field, Object value) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " > '");
        this.getMyQuery().getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orGe(String field, Object value) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " >= '");
        this.getMyQuery().getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orLike(String field, Object value) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " like '%");
        this.getMyQuery().getWhereSql().append(value + "%')");
        return this;
    }

    public WhereQuery orIn(String field, List list) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " in (");
        this.getMyQuery().getWhereSql().append(getInValue(list));
        this.getMyQuery().getWhereSql().append("))");
        return this;
    }

    public WhereQuery orIn(String field, String selectSql) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " in (");
        this.getMyQuery().getWhereSql().append(selectSql);
        this.getMyQuery().getWhereSql().append("))");
        return this;
    }

    public WhereQuery orNotIn(String field, List list) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " not in (");
        this.getMyQuery().getWhereSql().append(getInValue(list));
        this.getMyQuery().getWhereSql().append("))");
        return this;
    }

    public WhereQuery orNotIn(String field, String selectSql) {
        or();
        this.getMyQuery().getWhereSql().append(" or " + MyStringUtils.getSqlWord(field)  + " not in (");
        this.getMyQuery().getWhereSql().append(selectSql);
        this.getMyQuery().getWhereSql().append("))");
        return this;
    }

    public WhereQuery orSql(String orSql) {
        this.getMyQuery().getWhereSql().append(" and (");
        this.getMyQuery().getWhereSql().append(orSql);
        this.getMyQuery().getWhereSql().append(")");
        return this;
    }

    private WhereQuery or() {
        StringBuilder sb = this.getMyQuery().getWhereSql();

        if (sb.indexOf("and") > 0) {
            String str = "and ";
            sb = sb.replace(sb.lastIndexOf(str), sb.lastIndexOf(str) + str.length(), "and (");
        } else {
            String str = "where ";
            sb = sb.replace(sb.lastIndexOf(str), sb.lastIndexOf(str) + str.length(), " where (");
        }
        this.getMyQuery().setWhereSql(sb);
        return this;
    }


    public String toSql() {
        return this.getMyQuery().getWhereSql().toString();
    }

    public Integer deleteExecution() {
        return this.lDelete.execution(this);
    }


}
