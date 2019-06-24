package com.learn.hibernate.Service;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.base.LQuery;
import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

//        var t = lQuery.find(Card.class)
//                .join("employee", JoinType.LEFT)
//                .followUp("organization",JoinType.LEFT)
//                .fetch()
//                .select("id,cardNo,cardSn," +
//                        "state,createdUserName,lastUpdateUserName," +
//                        "createdDateTime,lastUpdateDateTime,employeeId," +
//                        "employee.name,employee.userNo,employee.state," +
//                        "employee.organization.id,employee.organization.name")
//                .where(pageData)
//                .asDto()
////                .findPage();
//
//        var t = lQuery.find(CardRecord.class)
//                .join("card",JoinType.LEFT)
//                .followUp("employee", JoinType.LEFT)
//                .followUp("organization",JoinType.LEFT)
//                .fetch()
//                .select("id,type,card.cardNo,card.cardSn," +
//                        "card.employee.state,createdUserName," +
//                        "createdDateTime," +
//                        "card.employee.name,card.employee.userNo," +
//                        "card.employee.organization.name")
//                .where(pageData)
//                .asDto()
//                .findPage();
//        lQuery.getBaseDao().init(CardRecord.class);
//        var t = lQuery.getBaseDao().getCb().equal(lQuery.getBaseDao().getRoot()
//                .join("card",JoinType.LEFT)
//                .join("employee",JoinType.LEFT)
//                .join("organization",JoinType.LEFT)
//                .get("id"),null);
//        System.out.println("sss");
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
//
//        Action action1 = new Action();
//        action1.setId("402880e76b7d61be016b7d61cbe50000");
//        action1.setCode("fdsfsdfsdfff");
//        action1.setDescription("dsfsdfsd");
//        action1.setName("ddd");
//        action1.setMenuId("ddddd");
////        var g = lQuery.save(action);
////
////
////        var h = lQuery.find(RoleAction.class)
////                .join("role", JoinType.LEFT)
////                .followUp("userRole",JoinType.INNER)
////                .justJoin("test",JoinType.INNER)
////                .fetch()
////                .select("id")
////                .findList();
////
//////        Action action = new Action();
////        action.setMenuId("2");
////        action.setId("1");
////        var j = lQuery.update(Action.class)
////                .where()
////                .eq("id","1")
////                .asUpdate()
////                .set(action)
////                .updateExecution();
//        List<Action> list = new ArrayList<>();
//        list.add(action);
//        list.add(action1);
//        lQuery.saveAll(list);


//        lMongo
//                .find(Employee.class)
//                .where().eq("name","ddd")
//                .notIn("id", Arrays.asList())
//                .findList();
//        var d = select();
//        var d = "";

        var d = lQuery.find(Employee.class)
                .eq("id",3)
                .findOne();
        return d;
    }


    @Transactional
    public Object select() {
        var d = lQuery
                .find(Employee.class)
                .fetchLeft("organization","id")
                .fetchLeft("department", "id")
                .eq("name","ddd")
                .or(new PageData("name_eq","ddd").add("id_eq",1L))
                .groupBy("id")
                .order("name,asc","id,desc")
                .findPage();
        return d;
    }
}
