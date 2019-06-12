package com.learn.hibernate.base;

import com.learn.hibernate.domian.PageData;
import lombok.Data;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Data
public class LJoin {

    private LSelect lSelect;

    private Join join;

    public LJoin(String tableName, JoinType joinType, LSelect lSelect) {
        this.lSelect = lSelect;
        Root root = this.lSelect.getBaseDao().getRoot();
        this.join = root.join(tableName, joinType);
    }

    public LJoin on(PageData pageData) {
        List<Predicate> predicates = this.lSelect.getBaseDao().getPredicates(pageData);
        this.join.on(predicates.toArray(Predicate[]::new));
        return this;
    }

    public LJoin followUp(String tableName, JoinType joinType) {
        this.join.join(tableName, joinType);
        return this;
    }

    public LJoin justJoin(String tableName, JoinType joinType) {
        this.lSelect.getBaseDao().getCb().equal(this.join.get("id"), null);
        this.join = null;
        this.join.join(tableName, joinType);
        return this;
    }


    public LSelect fetch() {
        this.lSelect.getBaseDao().getCb().equal(this.join.get("id"), null);
        this.join = null;
        return this.lSelect;
    }
}
