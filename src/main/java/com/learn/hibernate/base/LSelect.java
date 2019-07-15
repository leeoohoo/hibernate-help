package com.learn.hibernate.base;

import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.domian.PageInfo;
import com.learn.hibernate.enums.OrderType;
import lombok.Data;
import org.hibernate.sql.JoinType;
import org.hibernate.type.Type;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Data
@Scope(scopeName = "prototype")
@SuppressWarnings({"unused", "unchecked", "rawtypes", "null", "hiding"})
public class LSelect {


    private BaseDao baseDao;


    public LSelect(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public LSelect select(String fileds) {
        this.baseDao.setSelectFileds(fileds);
        return this;
    }

    public LSelect asDto() {
        this.baseDao.setT(false);
        return this;
    }

    public LSelect asDto(Class clazz) {
        this.baseDao.setT(false);
        this.baseDao.setDtoClass(clazz);
        return this;
    }

    public LSelect setPage(PageData pageData) {
        return this;
    }

    //-----------------------------------------------------------join------------------------------------------------------


    public LJoin join(String tableName, JoinType joinType) {
        return new LJoin(tableName, joinType, this);
    }

    public LSelect fetchInner(String tableName) {
        this.baseDao.getJoinTableName().add(tableName);
        this.baseDao.getJoinTypeMap().put(tableName, JoinType.INNER_JOIN);
        return this;
    }


    public LSelect fetchInner(String tableName, String joinId, PageData pageData) {
        this.baseDao.getJoinTableName().add(tableName);
        this.baseDao.getJoinTypeMap().put(tableName, JoinType.INNER_JOIN);
        this.baseDao.getStringSimpleExpressionMap().put(tableName, this.baseDao.initWhere(pageData));
        return this;
    }


    public LSelect order(String... fileds) {
        for (var str : fileds) {
            String[] split = str.split(",");
            switch (split[0]) {
                case "asc":
                    this.baseDao.getOrderTypeMap().put(split[1], OrderType.ASC);
                    break;
                case "desc":
                    this.baseDao.getOrderTypeMap().put(split[1], OrderType.DESC);
                    break;
            }

        }
        return this;
    }

    public LSelect groupBy(String fileds) {
        this.baseDao.getGroupFileds().append(","+fileds);
        return this;
    }


    public LSelect fetchLeft(String tableName, String joinId) {
        this.baseDao.getJoinTableName().add(tableName);
        this.baseDao.getJoinTypeMap().put(tableName, JoinType.LEFT_OUTER_JOIN);
        return this;
    }

    public LSelect fetchLeft(String tableName, String joinId, PageData pageData) {
        this.baseDao.getJoinTableName().add(tableName);
        this.baseDao.getJoinTypeMap().put(tableName, JoinType.LEFT_OUTER_JOIN);
        this.baseDao.getStringSimpleExpressionMap().put(tableName, this.baseDao.initWhere(pageData));
        return this;
    }


    public LSelect fetchRight(String tableName, String joinId) {
        this.baseDao.getJoinTableName().add(tableName);
        this.baseDao.getJoinTypeMap().put(tableName, JoinType.LEFT_OUTER_JOIN);
        return this;
    }

    public LSelect fetchRight(String tableName, String joinId, PageData pageData) {
        this.baseDao.getJoinTableName().add(tableName);
        this.baseDao.getJoinTypeMap().put(tableName, JoinType.LEFT_OUTER_JOIN);
        this.baseDao.getStringSimpleExpressionMap().put(tableName, this.baseDao.initWhere(pageData));
        return this;
    }

//-----------------------------------------------------------join------------------------------------------------------

    public Object findOne() {
        return this.baseDao.getOne();
    }

    public List findList() {
        return this.baseDao.getList();
    }

    public PageInfo findPage() {
        return this.baseDao.getPage();
    }


    //------------------------------------------------------------where-------------------------------------------------------

    public LSelect where(PageData pageData) {
        this.baseDao.getPageData().setRows(pageData.getRows());
        this.baseDao.getPageData().setPageIndex(pageData.getPageIndex());
        this.baseDao.getPageData().setMaxRows(pageData.getMaxRows());
        this.baseDao.getPageData().putAll(pageData);
        return this;
    }

    public LSelect eq(String filed, Object value) {
        filed += "_eq";
        this.baseDao.getPageData().put(filed,value);
        return this;
    }


    public LSelect like(String filed, String value) {
        filed += "_like";
        this.baseDao.getPageData().put(filed,value);
        return this;
    }

    public LSelect ge(String filed, Number value) {
        filed += "_ge";
        this.baseDao.getPageData().put(filed,value);
        return this;
    }


    public LSelect gt(String filed, Number value) {
        filed += "_gt";
        this.baseDao.getPageData().put(filed,value);
        return this;
    }


    public LSelect le(String filed, Number value) {
        filed += "_le";
        this.baseDao.getPageData().put(filed,value);
        return this;
    }


    public LSelect lt(String filed, Number value) {
        filed += "_lt";
        this.baseDao.getPageData().put(filed,value);
        return this;
    }


    public LSelect in(String filed, List value) {
        filed += "_in";
        this.baseDao.getPageData().put(filed,value);
        return this;
    }

    public LSelect notIn(String filed, List value) {
        filed += "_notIn";
        this.baseDao.getPageData().put(filed,value);
        return this;
    }

    public LSelect sql(String sql, Object[] values, Type[] types) {
        return this;
    }

    public LSelect or(PageData pageData) {
        this.baseDao.getOrPageData().add(pageData);
        return this;
    }

    //------------------------------------------------------------where-------------------------------------------------------
}
