package com.learn.hibernate.Service;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.base.LQuery;
import com.learn.hibernate.common.entity.Employee;
import com.learn.hibernate.domian.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


@Service
public class EmployeeService {


    private final LQuery lQuery;
    private final BaseDao baseDao;

    public EmployeeService(LQuery lQuery, BaseDao baseDao) {
        this.lQuery = lQuery;
        this.baseDao = baseDao;
    }

    @Transactional
    public Object get() throws ClassNotFoundException, InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
//        var a = lQuery
//                .find(Employee.class)
//                .fetchRight("organ","id")
//                .fetchLeft("dempatment", "id")
//                .asDto()
//                .groupBy("id")
//                .order("name,asc","id,desc")
//                .findPage();
//
//
//        lQuery.find(Employee.class)
//                .asDto()
//                .where(new PageData())
//                .eq("organ.path","ddd")
//                .findList();
//
//
//
//        var b = lQuery
//                .delete(Employee.class)
//                .where()
//                .eq("id",1)
//                .ge("sdfds",1)
//                .in("id",new ArrayList<>())
//                .deleteExecution();
//
//        var c = lQuery
//                .update(Employee.class)
//                .where()
//                .eq("name", "ssss")
//                .asUpdate()
//                .set("name","dffd")
//                .set(new PageData())
//                .updateExecution();
//
////        baseDao.init("Employee");
////        baseDao.getCb().equal(baseDao.getRoot().join("organ").get("id"),null);
////        var result = baseDao.getDtoOrTList(new PageData(), true);
////        return result;
//
//        var d = lQuery.customize(Employee.class)
//                .find("select e.id, e.name, o.name, d.name from employee e " +
//                        " left join organ o on o.id = e.organ_id" +
//                        " left join organ d on o.id = e.dempartment_id")
//                .asMap()
//                .asMapping("id,name,organName,dempartmentName")
//                .findPage();



        Employee employee = new Employee();
        employee.setName("dfsfdsf000");
        employee.setDempartmentId(3L);
        employee.setOrganId(4L);
        var d = lQuery.save(employee);



        return d;
    }
}
