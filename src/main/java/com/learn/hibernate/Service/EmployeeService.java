package com.learn.hibernate.Service;

import com.learn.hibernate.base.LQuery;
import com.learn.hibernate.common.entity.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;


@Service
public class EmployeeService {


    private final LQuery lQuery;

    public EmployeeService(LQuery lQuery) {
        this.lQuery = lQuery;
    }

    @Transactional
    public Object get() throws ClassNotFoundException, InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
//        var a = lQuery
//                .find(Employee.class)
//                .fetchLeft("organ","id")
//                .fetchLeft("dempatment", "id")
//                .asDto()
//                .groupBy("id")
//                .order("name,asc")
//                .findPage();
//
//        var b = lQuery.delete(Employee.class)
//                .where()
//                .eq("id",1)
//                .deleteExecution();
//
//        var c = lQuery
//                .update(Employee.class)
//                .where()
//                .eq("name", "ssss")
//                .asUpdate()
//                .set("name","dffd")
//                .updateExecution();
//
//        baseDao.init("Employee");
//        baseDao.getCb().equal(baseDao.getRoot().join("organ").get("id"),null);
//        var result = baseDao.getDtoOrTList(new PageData(), true);
//        return result;

        var d = lQuery.customize(Employee.class)
                .find("select e.id, e.name, o.name, d.name from employee e " +
                        " left join organ o on o.id = e.organ_id" +
                        " left join organ d on o.id = e.dempartment_id")
                .asDTO()
                .findList();


        return d;
    }
}
