package com.learn.hibernate.Service;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.domian.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EmployeeService {

    private final BaseDao baseDao;

    public EmployeeService(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Transactional
    public Object get() throws ClassNotFoundException {
        baseDao.init("Employee");
        baseDao.getCb().equal(baseDao.getRoot().join("organ").get("id"),null);
        var result = baseDao.getDtoOrTList(new PageData("organ.name_eq","sss"), true);
        return result;
    }
}
