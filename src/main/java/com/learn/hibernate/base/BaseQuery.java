package com.learn.hibernate.base;

import com.learn.hibernate.utils.MyStringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@Data
@Component
@Slf4j
public class BaseQuery<T> {

    @Autowired
    private EntityManager entityManager;

    protected Session session;



    public void initSession() {
        this.session = this.entityManager.unwrap(Session.class);
    }

    protected MyQuery myQuery = new MyQuery();

    public BaseQuery init(Class aClass) {
        this.myQuery.setAClass(aClass);
        this.myQuery.setUpdateSql(new StringBuilder());
        this.myQuery.setWhereSql(new StringBuilder());
        initSession();
        return this;
    }


    public WhereQuery where(){
        return new WhereQuery(this.myQuery,this.session);
    }

    public WhereQuery where(LDelete lDelete){
        return new WhereQuery(lDelete);
    }

    public UpdateQuery asUpdate(){
        return new UpdateQuery(this.myQuery,this.session);
    }

    public Integer updateExecution(){
        var updateSql = MyStringUtils.removeStringLastString(this.myQuery.getUpdateSql(), ",");
        var sql = updateSql + this.myQuery.getWhereSql();
        log.info(sql);
        var q = this.session.createNativeQuery(sql);
        return q.executeUpdate();
    }




    /**
     * 根据用户输入的查询字断添加表名和别名
     * @param fileds
     * @return
     */
    protected String getSelectFildes(String fileds){
        StringBuilder sb = new StringBuilder();
        Arrays.asList(fileds.split(",")).forEach(
                f -> {
                    if(!f.contains(".")){
                        sb.append(getCurrentTableName()+"."+f);

                    }else {
                        sb.append(f);
                    }
                    sb.append(" "+ RandomStringUtils.randomAlphanumeric(7));
                    sb.append(", ");
                }
        );
        return sb.substring(0,sb.lastIndexOf(","));
    }

    protected String getCurrentTableName() {
        var entityName = MyStringUtils.subStringLastChar(this.myQuery.getAClass().getName(), '.');
        var tableName = MyStringUtils.getSqlWord(entityName);
        return tableName;
    }

    protected String getInValue(List<Object> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(
                v -> {
                    sb.append("'"+v+ "',");
                }
        );
        return MyStringUtils.removeStringLastString(sb,",");
    }


}
