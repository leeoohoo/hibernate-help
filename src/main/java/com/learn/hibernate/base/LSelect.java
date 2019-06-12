package com.learn.hibernate.base;

import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.domian.PageInfo;
import com.learn.hibernate.utils.ClassUtils;
import lombok.Data;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.List;

@Data
public class LSelect {


    private  BaseDao baseDao;

    private PageData pageData;

    private boolean isT;

    private String[] fileds;



    public LSelect(BaseDao baseDao) {
        this.baseDao = baseDao;
        this.pageData = new PageData();
        this.isT = true;
    }

    public LSelect select(String fileds) {
        this.fileds = fileds.split(",");
        return this;
    }

    public LSelect asDto() {
        this.isT = false;
        return this;
    }

    public LSelect asDto(Class clazz) {
        this.baseDao.setDtoClass(clazz);
        this.isT = false;
        return this;
    }

    public LSelect setPage(PageData pageData) {
        PageData p = pageData;
        p.putAll(this.pageData);
        this.pageData = p;
        return this;
    }

    //-----------------------------------------------------------join------------------------------------------------------

    public LSelect fetchInner(String tableName, String joinId) {
        this.baseDao.getCb().equal(this.getBaseDao().getRoot().join(tableName, JoinType.INNER).get(joinId),null);
        return this;
    }
    public LSelect fetchInner(String tableName, String joinId,PageData pageData) {
        List<Predicate> predicates = this.baseDao.getPredicates(pageData);
        this.baseDao.getCb().equal(this.getBaseDao().getRoot().join(tableName, JoinType.INNER).on(predicates.toArray(Predicate[]::new)).get(joinId),null);
        return this;
    }


    public LSelect order(String... fileds) {
        this.baseDao.setOrderBy(fileds);
        return this;
    }
    public LSelect groupBy(String fileds) {
        this.baseDao.setGroupBy(fileds.split(","));
        return this;
    }


    public LSelect fetchLeft(String tableName, String joinId) {
        this.baseDao.getCb().equal(this.getBaseDao().getRoot().join(tableName, JoinType.LEFT).get(joinId),null);
        return this;
    }
    public LSelect fetchLeft(String tableName, String joinId,PageData pageData) {
        List<Predicate> predicates = this.baseDao.getPredicates(pageData);
        this.baseDao.getCb().equal(this.getBaseDao().getRoot().join(tableName, JoinType.LEFT).on(predicates.toArray(Predicate[]::new)).get(joinId),null);
        return this;
    }


    public LSelect fetchRight(String tableName, String joinId) {
        this.baseDao.getCb().equal(this.getBaseDao().getRoot().join(tableName, JoinType.RIGHT).get(joinId),null);
        return this;
    }
    public LSelect fetchRight(String tableName, String joinId,PageData pageData) {
        List<Predicate> predicates = this.baseDao.getPredicates(pageData);
        this.baseDao.getCb().equal(this.getBaseDao().getRoot().join(tableName, JoinType.RIGHT).on(predicates.toArray(Predicate[]::new)).get(joinId),null);
        return this;
    }

//-----------------------------------------------------------join------------------------------------------------------

    public Object findOne() throws ClassNotFoundException {
        if(!this.pageData.containsKey("id_eq") || this.pageData.get("id_eq") == null) {
            throw new RuntimeException("缺少主键条件");
        }
        var result = this.baseDao.getInfoDtoOrT(this.pageData.get("id_eq"),this.isT,this.pageData);
        if(this.isT) {
            return result.getT();
        }else {
            return result.getDto();
        }
    }

    public List findList() throws ClassNotFoundException {
        var result = this.baseDao.getDtoOrTList(this.pageData,this.isT);
        if(this.isT) {
            return result.getTList();
        }else {
            return result.getDtoList();
        }
    }

    public PageInfo findPage() throws ClassNotFoundException {
        var result = this.baseDao.getPageInfo(this.pageData, this.isT,this.fileds);
        return result;
    }




    //------------------------------------------------------------where-------------------------------------------------------

   public LSelect where(PageData pageData) {
        this.pageData.putAll(pageData);
        return this;
   }

    public LSelect eq(String filed, Object value) {
        this.pageData.put(filed+"_eq", value);
        return this;
    }


    public LSelect like(String filed, String value) {
        this.pageData.put(filed+"_like", value);
        return this;
    }

    public LSelect ge(String filed, Number value) {
        this.pageData.put(filed+"_ge", value);
        return this;
    }


    public LSelect gt(String filed, Number value) {
        this.pageData.put(filed+"_gt", value);
        return this;
    }


    public LSelect le(String filed, Number value) {
        this.pageData.put(filed+"_le", value);
        return this;
    }


    public LSelect lt(String filed, Number value) {
        this.pageData.put(filed+"_lt", value);
        return this;
    }


    public LSelect in(String filed, List value) {
        this.pageData.put(filed+"_in", value);
        return this;
    }

    public LSelect notIn(String filed, List value) {
        this.pageData.put(filed+"_notIn", value);
        return this;
    }

    public LSelect or(PageData pageData){
        this.baseDao.setOrPageData(pageData);
        return this;
    }

    //------------------------------------------------------------where-------------------------------------------------------
}
