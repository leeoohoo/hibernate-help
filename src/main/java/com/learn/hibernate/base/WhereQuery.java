package com.learn.hibernate.base;

import lombok.Data;
import org.hibernate.Session;

import java.util.List;

@Data
public class WhereQuery extends BaseQuery {

    private LDelete lDelete;

    public WhereQuery(MyQuery myQuery, Session session) {
        this.session = session;
        this.myQuery = myQuery;
        this.myQuery.setWhereSql(new StringBuilder(" where 1 = 1"));
    }

    public WhereQuery(LDelete lDelete) {
        this.lDelete = lDelete;
        this.myQuery.setWhereSql(new StringBuilder(" where 1 = 1"));
    }

    public WhereQuery in(String field, List<Object> list) {
        if (null != list && list.size() > 0) {
            this.myQuery.getWhereSql().append(" and " + field + " in (");
            this.myQuery.getWhereSql().append(getInValue(list));
            this.myQuery.getWhereSql().append(")");
        }
        return this;
    }

    public WhereQuery in(String field, String selectSql) {
        this.myQuery.getWhereSql().append(" and " + field + " in (");
        this.myQuery.getWhereSql().append(selectSql);
        this.myQuery.getWhereSql().append(")");
        return this;
    }

    public WhereQuery notIn(String field, String selectSql) {
        this.myQuery.getWhereSql().append(" and " + field + " not in (");
        this.myQuery.getWhereSql().append(selectSql);
        this.myQuery.getWhereSql().append(")");
        return this;
    }

    public WhereQuery notIn(String field, List<Object> list) {
        if(null != list && list.size() > 0) {
            this.myQuery.getWhereSql().append(" and " + field + " not in(");
            this.myQuery.getWhereSql().append(getInValue(list));
            this.myQuery.getWhereSql().append(")");
        }
        return this;
    }

    public WhereQuery eq(String field, Object value) {
        this.myQuery.getWhereSql().append(" and " + field + " = '");
        this.myQuery.getWhereSql().append(value + "'");
        return this;
    }

    public WhereQuery lt(String field, Number value) {
        this.myQuery.getWhereSql().append(" and " + field + " < ");
        this.myQuery.getWhereSql().append(value);
        return this;
    }


    public WhereQuery le(String field, Number value) {
        this.myQuery.getWhereSql().append(" and " + field + " <= ");
        this.myQuery.getWhereSql().append(value);
        return this;
    }

    public WhereQuery gt(String field, Number value) {
        this.myQuery.getWhereSql().append(" and " + field + " > ");
        this.myQuery.getWhereSql().append(value);
        return this;
    }

    public WhereQuery ge(String field, Number value) {
        this.myQuery.getWhereSql().append(" and " + field + " >= ");
        this.myQuery.getWhereSql().append(value);
        return this;
    }

    public WhereQuery like(String field, String value) {
        this.myQuery.getWhereSql().append(" and " + field + " like '%");
        this.myQuery.getWhereSql().append(value + "%'");
        return this;
    }

    public WhereQuery orEq(String field, Object value) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " = '");
        this.myQuery.getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orLt(String field, Object value) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " < '");
        this.myQuery.getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orLe(String field, Object value) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " <= '");
        this.myQuery.getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orGt(String field, Object value) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " > '");
        this.myQuery.getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orGe(String field, Object value) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " >= '");
        this.myQuery.getWhereSql().append(value + "')");
        return this;
    }

    public WhereQuery orLike(String field, Object value) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " like '%");
        this.myQuery.getWhereSql().append(value + "%')");
        return this;
    }

    public WhereQuery orIn(String field, List<Object> list) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " in (");
        this.myQuery.getWhereSql().append(getInValue(list));
        this.myQuery.getWhereSql().append("))");
        return this;
    }

    public WhereQuery orIn(String field, String selectSql) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " in (");
        this.myQuery.getWhereSql().append(selectSql);
        this.myQuery.getWhereSql().append("))");
        return this;
    }

    public WhereQuery orNotIn(String field, List<Object> list) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " not in (");
        this.myQuery.getWhereSql().append(getInValue(list));
        this.myQuery.getWhereSql().append("))");
        return this;
    }

    public WhereQuery orNotIn(String field, String selectSql) {
        or();
        this.myQuery.getWhereSql().append(" or " + field + " not in (");
        this.myQuery.getWhereSql().append(selectSql);
        this.myQuery.getWhereSql().append("))");
        return this;
    }

    public WhereQuery orSql(String orSql) {
        this.myQuery.getWhereSql().append(" and (");
        this.myQuery.getWhereSql().append(orSql);
        this.myQuery.getWhereSql().append(")");
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
