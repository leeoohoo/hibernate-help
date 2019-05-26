package com.learn.hibernate.Service;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.domian.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

@Service
public class PlanService {
    private final BaseDao baseDao;

    public PlanService(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Transactional
    public Object getList() throws ClassNotFoundException {
        baseDao.init("Plan");
        baseDao.getCb().equal(baseDao.getRoot().join("project").on(baseDao.getPredicateEq("name","11")).get("id"), null);
        baseDao.setOrderBy("id,asc");
        baseDao.getCq().groupBy(baseDao.getRoot().get("id"),
                baseDao.getRoot().get("name"),
                baseDao.getRoot().get("projectId"),
                baseDao.getRoot().get("project").get("name"));

        var result = baseDao.getDtoOrTList(new PageData("project.name_eq",""),false);
        return result;
    }

}
