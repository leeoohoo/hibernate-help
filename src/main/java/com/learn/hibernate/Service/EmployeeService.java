package com.learn.hibernate.Service;

import com.learn.hibernate.base.LQuery;
import com.learn.hibernate.common.entity.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EmployeeService {

//    private final BaseDao baseDao;
//
//    public EmployeeService(BaseDao baseDao) {
//        this.baseDao = baseDao;
//    }

    private final LQuery lQuery;

    public EmployeeService(LQuery lQuery) {
        this.lQuery = lQuery;
    }

    @Transactional
    public Object get() throws ClassNotFoundException {
        var a = lQuery
                .find(Employee.class)
                .fetchLeft("organ","id")
                .fetchInner("dempatment", "id")
                .asDto()
                .eq("id",1)
                .findOne();

        var b = lQuery.delete(Employee.class)
                .where()
                .eq("id",1)
                .deleteExecution();

        var c = lQuery
                .update(Employee.class)
                .where()
                .eq("name", "ssss")
                .asUpdate()
                .set("name","dffd")
                .updateExecution();
//
//        baseDao.init("Employee");
//        baseDao.getCb().equal(baseDao.getRoot().join("organ").get("id"),null);
//        var result = baseDao.getDtoOrTList(new PageData(), true);
//        return result;
        return null;
    }
}
