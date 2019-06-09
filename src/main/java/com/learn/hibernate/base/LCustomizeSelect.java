package com.learn.hibernate.base;

import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.domian.PageInfo;
import com.learn.hibernate.utils.ClassUtils;
import com.learn.hibernate.utils.MyStringUtils;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class LCustomizeSelect {

    private BaseDao baseDao;

    private String sql;

    private NativeQuery nativeQuery;

    private Class dtoClass;

    private Class tClass;

    private boolean isT = true;

    private boolean isMap;

    private Session session;

    private PageData pageData = new PageData();

    private boolean isGroup;

    private String selectFileds;

    public LCustomizeSelect(BaseDao baseDao, String sql) {
        this.baseDao = baseDao;
        this.sql = sql;
        this.setAlias();
        this.session = this.baseDao.getBaseQuery().getSession();
        this.nativeQuery = this.session.createNativeQuery(this.sql);
        this.dtoClass = baseDao.getDtoClass();
        this.tClass = baseDao.getTClass();
    }

    public LCustomizeSelect asDTO() {
        this.isT = false;
        return this;
    }

    public LCustomizeSelect asMap() {
        this.isMap = true;
        return this;
    }

    public LCustomizeSelect asMapping(String fileds) {
        this.selectFileds = fileds;
        return this;
    }

    public LCustomizeSelect asDTO(Class clazz) {
        this.isT = false;
        this.dtoClass = clazz;
        return this;
    }

    public LCustomizeSelect setPage(PageData pageData) {
        this.pageData = pageData;
        return this;
    }

    public List findList() throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return getList(this.nativeQuery.getResultList());
    }


    public Object findOne() throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var result = getList(this.nativeQuery.getResultList());
        if(result.size() > 0) {
            return result.get(0);
        }else {
            return null;
        }
    }

    public PageInfo findPage() throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
        StringBuilder sb = new StringBuilder(this.sql.toLowerCase());
        var countSql = sb.replace(sb.indexOf("select")+"select".length(), sb.indexOf("from")," count(*) ");
        if(this.sql.contains("group by")) {
            this.isGroup = true;
        }
        var countQuery = this.session.createNativeQuery(countSql.toString());
        PageInfo pageInfo = new PageInfo(pageData,countQuery,this.isGroup);
        this.nativeQuery.setFirstResult(this.pageData.getPageIndex());
        this.nativeQuery.setMaxResults(this.pageData.getMaxRows());
        var result = findList();
        pageInfo.setList(result);
        return pageInfo;
    }




    private void setAlias(){
        StringBuilder sb = new StringBuilder(this.sql.toLowerCase());
        StringBuilder newsb = new StringBuilder(" ");
        var fileds = getSelectFileds();
        for(String filed : fileds) {
            newsb.append(filed + " as "+ RandomStringUtils.randomAlphanumeric(7)+ ",");
        }
        var alias = MyStringUtils.removeStringLastString(newsb,",");
        this.sql = sb.replace(sb.indexOf("select")+"select".length(), sb.indexOf(" from"),alias).toString();
    }

    private String[] getSelectFileds() {
        StringBuilder sb = new StringBuilder(this.sql.toLowerCase());
        var sql = sb.substring(sb.indexOf("select")+"select".length(),sb.indexOf("from"));
        var fileds = sql.split(",");
        return fileds;
    }





    private List getList(List list) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
        List resultList = new ArrayList();
        for(Object o : list) {
            if(o instanceof Object[]) {
                var result = getResult((Object[])o);
                resultList.add(result);
            }
        }
        return resultList;
    }

    private Object getResult(Object[] values) throws IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
        if(values == null) {
            return null;
        }
        if(isMap) {
            return getMap(values);
        }else {
            return getDtoOrT(values);
        }

    }

    public Map<String, Object> getMap(Object[] values) {
        Map<String, Object> map = new HashMap();
        String[] selectFileds = null;
        if(this.selectFileds.length() <= 0) {
            selectFileds = getSelectFileds();

        }else {
            selectFileds = this.selectFileds.split(",");
        }
        for(int i = 0; i<values.length; i++) {
            var filedName = selectFileds[i];
            var value = values[i];
            map.put(filedName,value);
        }
        return map;
    }

    public Object getDtoOrT(Object[] values) throws IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
        Class c;
        if(isT) {
            c = this.tClass;
        }else {
            c = this.dtoClass;
        }
        var fields = c.getDeclaredFields();
        var result = c.newInstance();
        for(int i = 0; i < values.length; i++) {
            var fieldName = fields[i].getName();
            var value = values[i];
            var type = fields[i].getType();
            ClassUtils.setProperty(result,fieldName,value,type);
        }
        return result;
    }

}
