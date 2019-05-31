package com.learn.hibernate.Service;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.base.BaseQuery;
import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.entity.Plan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class PlanService {


    private final BaseDao baseDao;

    public PlanService(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Transactional
    public Object getList() throws ClassNotFoundException {
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
