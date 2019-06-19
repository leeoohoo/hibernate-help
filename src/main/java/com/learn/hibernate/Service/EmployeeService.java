package com.learn.hibernate.Service;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.base.LQuery;
import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.entity.Action;
import com.learn.hibernate.entity.Card;
import com.learn.hibernate.entity.RoleAction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
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
    public Object get(PageData pageData) throws ClassNotFoundException, IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException {
//        var d = lQuery
//                .find(Employee.class)
//                .fetchLeft("organ","id")
//                .fetchLeft("dempatment", "id")
//                .eq("name","ddd")
//                .or(new PageData("name_eq","ddd").add("id_eq",1L))
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

//        baseDao.init("Employee");
//        baseDao.getCb().equal(baseDao.getRoot().join("organ").get("id"),null);
//        var result = baseDao.getDtoOrTList(new PageData(), true);
//        return result;

//        var t = lQuery.customize(Employee.class)
//                .find("select e.id, e.name, o.name, d.name from employee e " +
//                        " left join organ o on o.id = e.organ_id" +
//                        " left join organ d on d.id = e.dempartment_id")
////                .asMap()
////                .asMapping("id,name,organName,dempartmentName")
//                .findPage();

        var t = lQuery.find(Card.class)
                .join("employee", JoinType.LEFT)
                .followUp("organization",JoinType.LEFT)
                .fetch()
                .select("id,cardNo,cardSn," +
                        "state,createdUserName,lastUpdateUserName," +
                        "createdDateTime,lastUpdateDateTime,employeeId," +
                        "employee.name,employee.userNo,employee.state," +
                        "employee.organization.id,employee.organization.name")
                .where(pageData)
                .asDto()
                .findPage();
//
//        var l = lQuery.find(RoleAction.class).findList();
//
//        Employee employee = new Employee();
//        employee.setName("dfsfdsf000");
//        employee.setDempartmentId(3L);
//        employee.setOrganId(4L);
//        var f = lQuery.save(employee);
//
//        Action action = new Action();
//        action.setCode("ddd");
//        action.setDescription("eeee");
//        action.setName("ddd");
//        action.setMenuId("ddddd");
//        var g = lQuery.save(action);
//
//
//        var h = lQuery.find(RoleAction.class)
//                .join("role", JoinType.LEFT)
//                .followUp("userRole",JoinType.INNER)
//                .justJoin("test",JoinType.INNER)
//                .fetch()
//                .select("id")
//                .findList();
//
////        Action action = new Action();
//        action.setMenuId("2");
//        action.setId("1");
//        var j = lQuery.update(Action.class)
//                .where()
//                .eq("id","1")
//                .asUpdate()
//                .set(action)
//                .updateExecution();


        return t;
    }
}
