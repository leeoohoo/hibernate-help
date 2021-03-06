package com.learn.hibernate.oldbase;//package com.learn.hibernate.base;
//
//import com.learn.hibernate.domian.PageData;
//import lombok.Data;
//import org.springframework.context.annotation.Scope;
//
//import javax.persistence.criteria.*;
//import java.util.List;
//
//@Data
//@Scope(scopeName = "prototype")
//public class LJoin {
//
//    private LSelect lSelect;
//
//    private Join join;
//
//    public LJoin(String tableName, JoinType joinType, LSelect lSelect) {
//        this.lSelect = lSelect;
//        Root root = this.lSelect.getBaseDao().getRoot();
//        this.join = root.join(tableName, joinType);
//    }
//
//    public LJoin on(PageData pageData) {
//        List<Predicate> predicates = this.lSelect.getBaseDao().getPredicates(pageData);
//        this.join.on(predicates.toArray(Predicate[]::new));
//        return this;
//    }
//
//    public LJoin followUp(String tableName, JoinType joinType) {
//        this.join = this.join.join(tableName, joinType);
//        return this;
//    }
//
//    public LJoin justJoin(String tableName, JoinType joinType) {
//        this.lSelect.getBaseDao().getCb().equal(this.join.get("id"), null);
//        this.join = null;
//        this.join.join(tableName, joinType);
//        return this;
//    }
//
//
//    public LSelect fetch() {
//        this.lSelect.getBaseDao().getCb().equal(this.join.get("id"), null);
//        this.join = null;
//        return this.lSelect;
//    }
//}
