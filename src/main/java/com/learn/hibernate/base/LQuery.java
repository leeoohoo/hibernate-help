package com.learn.hibernate.base;

import com.learn.hibernate.utils.SpringUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@SuppressWarnings({"unused", "unchecked", "rawtypes", "null", "hiding"})
@Data
@Component
@Scope(scopeName = "prototype")
public class LQuery<T> {

    @Autowired
    private  BaseDao baseDao;



    public LQuery() {
        this.baseDao = (BaseDao) SpringUtil.getBean("baseDao");
    }

    private static BaseDao getBaseDao() {
        return (BaseDao) SpringUtil.getBean("baseDao");
    }

    private static BaseDao initBaseDao(Class clz) {
        var baseDao = getBaseDao();
        baseDao.init(clz);
        return baseDao;
    }

    public   Object save(T t) {
        var baseDao = getBaseDao();
        baseDao.getBaseQuery().initSession();
        baseDao.setTClass(t.getClass());
        return baseDao.add(t);
    }

    public boolean saveAll(List<T> ts) {
        var baseDao = getBaseDao();
        baseDao.getBaseQuery().initSession();
        return baseDao.addBach(ts);
    }

    public static LSelect find(Class clz) {
        return new LSelect(initBaseDao(clz));
    }

    public static LCustomize customize(Class clz)  {
        return new LCustomize(initBaseDao(clz));
    }

    public static LDelete delete(Class clz)  {
        return new LDelete(initBaseDao(clz));
    }


    public static BaseQuery update(Class clz) {
        return getBaseDao().getBaseQuery();
    }






}
