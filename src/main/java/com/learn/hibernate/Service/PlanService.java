package com.learn.hibernate.Service;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.domian.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.Arrays;

@Service
public class PlanService {


    private final BaseDao baseDao;

    public PlanService(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Transactional
    public Object getList() throws ClassNotFoundException {

        var list = baseDao.getDtoOrTList(new PageData("name_eq","sss").add("age_ge", 1),true).getTList();

        var page = baseDao.getPageInfo(new PageData("name_eq","aaa"),false, "name","age","address,project.name");


        baseDao.getCb().equal(baseDao.getRoot().join("project", JoinType.LEFT).get("id"),null);
        var result = baseDao.getDtoOrTList(new PageData("name", "aaa"),true);

       return null;
    }

    @Transactional
    public Object save() throws ClassNotFoundException {
//        String sql = "update project set name = '1122' where id in ('1','2')";
//        var q = baseDao.getSession().createNativeQuery(sql);
//
//        var a = q.executeUpdate();
//        sql = "select plan.name ddd, project.name dddd from plan left join project  on project.id = plan.project_id";
//        var query = baseDao.getSession().createNativeQuery(sql);
//        query.setFlushMode(FlushMode.COMMIT);
//        var r = query.getResultList();

        baseDao.init("Plan");
        var v = baseDao.getBaseQuery()
                .where()
                .in("id",Arrays.asList(3,4))
                .eq("name","dd")
                .orGt("id",1)
                .orSql("id > 3 or id = 3")
                .orEq("name","dddf")
                .asUpdate()
                .set(new PageData("name","ddd"))
                .updateExecution();
        baseDao.delete(baseDao.getBaseQuery().where().eq("id",1L).toSql());

        return null;
    }



//
//    @Transactional
//    public Object delete() throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
//        baseDao.init("Plan");
//        return baseDao.deleteById(1L,false);
//    }


}
