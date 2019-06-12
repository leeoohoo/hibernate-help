package com.learn.hibernate.base;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@SuppressWarnings({"unused", "unchecked", "rawtypes", "null", "hiding"})
@Data
@Component
public class LQuery<T> {

    @Autowired
    private  BaseDao baseDao;


    public Object save(T t) {
        this.baseDao.getBaseQuery().initSession();
        this.baseDao.setTClass(t.getClass());
        return this.baseDao.add(t);
    }

    public boolean saveAll(List<T> ts) {
        this.baseDao.getBaseQuery().initSession();
        return this.baseDao.addBach(ts);
    }

    public LSelect find(Class clz) throws ClassNotFoundException {
        this.baseDao.init(clz);
        return new LSelect(this.baseDao);
    }

    public LCustomize customize(Class clz) throws ClassNotFoundException {
        this.baseDao.init(clz);
        return new LCustomize(this.baseDao);
    }

    public LDelete delete(Class clz) throws ClassNotFoundException {
        this.baseDao.init(clz);
        return new LDelete(this.baseDao);
    }


    public BaseQuery update(Class clz) throws ClassNotFoundException {
        this.baseDao.init(clz);
        return this.baseDao.getBaseQuery();
    }






}
