package com.learn.hibernate.base;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.domian.PageInfo;
import com.learn.hibernate.enums.OrderType;
import com.learn.hibernate.utils.MyStringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.util.*;

/**
 * @param <T>
 * @param <DTO>
 * @param <D>
 * @author lee
 */

@Component
@PropertySource("classpath:my_base_dao.properties")
@Scope(scopeName = "prototype")
@Data
@SuppressWarnings({"unused", "unchecked", "rawtypes", "null", "hiding"})
@Slf4j
public  class BaseDao<T, DTO, D>  {

    @Value("${mybasedao.entity}")
    private String entityString;

    @Value("${mybasedao.dto}")
    private String dtoString;

    @Value("${mybasedao.dto.suffix}")
    private String dtoSuffix;

    private String entityName;

    private boolean isT = true;

    private boolean isGroup;

    private Class<T> tClass;

    private Class<DTO> dtoClass;

    private List<PageData> orPageData ;//or括号中的条件

    private PageData pageData ;//查询条件

    private Class resultClass;//最终获得的对象类型

    private ProjectionList projectionList;

    private StringBuilder groupFileds;

    private Map<String, OrderType> orderTypeMap ;

    private List<Criterion> criterionList ;

    private Map<String, JoinType> joinTypeMap;

    private Map<String, SimpleExpression> stringSimpleExpressionMap ;

    private String selectFileds;

    private DetachedCriteria criteria;

    private boolean isPage;

    private List<String> joinTableName;

    @Autowired
    private BaseQuery baseQuery;


    public BaseDao() {
    }

    public void init(String clazz) {
        String dtoName = clazz + dtoSuffix;
        BaseDao.this.entityName = clazz;
        try {
            initTClz(clazz);
        } catch (ClassNotFoundException e) {
            log.error(clazz + ":找不到该实体类");
        }
        initDtoClz(dtoName);
        BaseDao.this.baseQuery = baseQuery.init(BaseDao.this.tClass);
        this.initSqlRely();
    }

    public  void init(Class<T> clazz) {
        BaseDao.this.tClass = clazz;
        var tableName = MyStringUtils.subStringLastChar(clazz.getName(), '.');
        BaseDao.this.entityName = tableName;
        String dtoName = tableName + dtoSuffix;
        initDtoClz(dtoName);
        BaseDao.this.baseQuery = baseQuery.init(BaseDao.this.tClass);
        this.initSqlRely();
    }

    private void initSqlRely () {
        this.joinTypeMap = new HashMap<>();
        this.orderTypeMap = new HashMap<>();
        this.stringSimpleExpressionMap = new HashMap<>();
        this.groupFileds = new StringBuilder();
        this.joinTableName = new ArrayList<>();
        this.orPageData = new ArrayList<>();
        this.pageData = new PageData();
    }

    public void initTClz(String clazz) throws ClassNotFoundException {
        var packagelist = Arrays.asList(BaseDao.this.entityString.split(","));
        int count = 0;
        for (String str : packagelist) {
            try {
                BaseDao.this.tClass = (Class<T>) Class.forName(str + "." + clazz);
            } catch (ClassNotFoundException e) {
                count++;
                if (packagelist.size() <= count) {
                    throw e;
                }
            }
        }
    }

    public Class getMyClass(String clazz) throws ClassNotFoundException {
        var packagelist = Arrays.asList(BaseDao.this.entityString.split(","));
        int count = 0;
        Class clz = null;
        for (String str : packagelist) {
            try {
                clz = Class.forName(str + "." + clazz);
            } catch (ClassNotFoundException e) {
                count++;
                if (packagelist.size() <= count) {
                    throw e;
                }
            }
        }
        return clz;
    }

    public void initDtoClz(String dtoName) {
        int count = 0;
        var packagelist = Arrays.asList(BaseDao.this.dtoString.split(","));
        for (String str : packagelist) {
            try {
                this.dtoClass = (Class<DTO>) Class.forName(str + "." + dtoName);
            } catch (ClassNotFoundException e) {
                log.error("-----------------------------------------------" + dtoName + "未找到相应的dto类");
            }
        }
    }

    public void init(Class<T> entityClz, Class<DTO> dtoClz) {
        BaseDao.this.entityName = MyStringUtils.subStringLastChar(entityClz.getName(), '.');
        BaseDao.this.tClass = entityClz;
        BaseDao.this.dtoClass = dtoClz;
        BaseDao.this.baseQuery = baseQuery.init(this.tClass);
    }


    public D add(T t) {
        Session session = this.baseQuery.getSession();
        var result = (D) session.save(t);
        session.flush();
        session.clear();
        return result;
    }

    public boolean addBach(List<T> list) {
        if (null == list && list.size() < 0) {
            return true;
        }
        var session = this.baseQuery.getSession();
        T t = null;
        for (int i = 0; i < list.size(); i++) {
            t = list.get(i);
            session.saveOrUpdate(t);
        }
        return true;

    }

    public Boolean updateById(T t, D id) {
        Integer result = null;
        try {
            result = this.baseQuery.asUpdate().set(t).where().eq("id", id).updateExecution();
            return result > 0;
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
            log.error(t.getClass().getName() + "转换map失败");
        } finally {
            return false;
        }
    }

    public Integer delete(String where) {
        var result = this.baseQuery.getSession().createQuery("delete " + entityName + where)
                .executeUpdate();
        return result;
    }

    public Integer deleteById(D id, boolean isDeleted) {
        if (isDeleted) {
            return deleteByIsDeleted(id);
        }
        var result = this.baseQuery.getSession().createQuery("delete " + entityName + " where id=?")
                .setParameter(0, id).executeUpdate();
        return result;
    }

    @Transactional
    public boolean deleteBach(String... ids) {
        var list = Arrays.asList(ids);
        for (String id : list) {
            var result = deleteById((D) id, false);
            if (result == null) {
                throw new RuntimeException();
            }
        }
        return true;
    }


    @Transactional
    public boolean deleteByIsDeletedBach(String... ids) {
        var list = Arrays.asList(ids);
        for (String id : list) {
            var result = deleteById((D) id, true);
            if (result == null) {
                throw new RuntimeException();
            }
        }
        return true;
    }


    public Integer deleteByIsDeleted(D id) {
        return BaseDao.this.baseQuery.where().eq("id", id).asUpdate().set("isDelete", 1).updateExecution();
    }

    protected PageData putId(D id, PageData pageData) {
        PageData myPageData = new PageData();
        myPageData.put("id_eq", id);
        if (pageData != null) {
            myPageData.putAll(pageData.getMap());
        }
        return myPageData;
    }


    private Criteria getResult() {
        this.initCriteria();
        Session session = BaseDao.this.baseQuery.getSession();
        return BaseDao.this.criteria.getExecutableCriteria(session);
    }

    private Criteria getPageResult() {
        this.isPage = true;
        this.initCriteria();
        this.isPage = false;
        this.initPageSelect();
        this.criteria.setResultTransformer(Transformers.aliasToBean(PageInfo.class));
        Session session = this.baseQuery.getSession();
        return this.criteria.getExecutableCriteria(session);
    }

    public Object getOne() {
        return BaseDao.this.getResult().uniqueResult();
    }

    public List getList() {
        this.getResult();
        return BaseDao.this.getResult().list();
    }

    public PageInfo getPage() {
        Criteria pageResult = BaseDao.this.getPageResult();
        PageInfo pageInfo = (PageInfo) pageResult.uniqueResult();
        pageInfo.init(this.pageData);
        this.criteria = DetachedCriteria.forClass(this.tClass);
        Criteria result = this.getResult();
        result.setFirstResult(this.pageData.getPageIndex());
        System.out.println("maxrows"+this.pageData.getMaxRows());
        System.out.println("rows"+this.pageData.getRows());
        System.out.println("pageindxe"+this.pageData.getPageIndex());
        result.setMaxResults(this.pageData.getMaxRows());
        pageInfo.setList(result.list());
        return pageInfo;
    }


    /**
     * 初始化sql
     */
    private void initCriteria() {
        this.criteria = DetachedCriteria.forClass(this.tClass);
        if(!this.isPage) {
            this.initSelect();//初始化查询字断
        }
        this.initJoin();
        this.initWhere(this.pageData);//初始化一般查询条件
        this.orWhere();//初始化or条件
        this.initOrder();
        this.initResult();

    }

    /**
     * 初始化查找总数
     */
    private void initPageSelect() {
        BaseDao.this.projectionList = Projections.projectionList();
        BaseDao.this.projectionList.add(Projections.count("id").as("count"));
        BaseDao.this.criteria.setProjection(projectionList);
    }

    /**
     * 初始化最终结果对象
     */
    private void initResult() {
        if (null != resultClass) {
            if (resultClass.getName().equals(new HashMap<>().getClass().getName())) {
                BaseDao.this.criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            } else if (resultClass.getName().equals(new ArrayList<>().getClass().getName())) {
                BaseDao.this.criteria.setResultTransformer(Transformers.TO_LIST);
            } else {
                BaseDao.this.criteria.setResultTransformer(Transformers.aliasToBean(resultClass));
            }
        } else if (isT) {
            BaseDao.this.criteria.setResultTransformer(Transformers.aliasToBean(this.tClass));
        } else {
            BaseDao.this.criteria.setResultTransformer(Transformers.aliasToBean(this.dtoClass));
        }
        System.out.println("initResult");
    }


    /**
     * 初始化排序
     */
    private void initOrder() {
        this.orderTypeMap.forEach(
                (k, v) -> {
                    switch (v) {
                        case ASC:
                            BaseDao.this.criteria.addOrder(Order.asc(k));
                            break;
                        case DESC:
                            BaseDao.this.criteria.addOrder(Order.desc(k));
                            break;
                    }
                }
        );
        System.out.println("initOrder");
    }


    /**
     * 初始化join关联
     */
    private void initJoin() {
        for(Map.Entry<String,JoinType> entry : BaseDao.this.joinTypeMap.entrySet()) {
            var k = entry.getKey();
            var v = entry.getValue();
            SimpleExpression simpleExpression = BaseDao.this.stringSimpleExpressionMap.get(k);
            int i = k.lastIndexOf(".");
            String substring = "";
            if(i < 0) {
                substring = k;
            }else {
                substring = k.substring(i+1);
            }
            if (null != simpleExpression) {
                BaseDao.this.criteria.createAlias(k, substring, v, simpleExpression);
            } else {
                BaseDao.this.criteria.createAlias(k, substring, v);
            }
        }
        System.out.println("initJoin");
    }

    /**
     * 初始化一般搜索条件
     *
     * @param pageData
     */
    public Criterion initWhere(PageData pageData) {
        this.criterionList = new ArrayList<>();
        Criterion criterion = null;
        for (Map.Entry<String, Object> m : pageData.getMap().entrySet()) {
            var k = m.getKey();
            var v = m.getValue();
            var stes = k.split("_");
            if (stes.length > 1) {
                switch (stes[1]) {
                    case "like":
                        if (v != null && !"".equals(v.toString().trim())) {
                            criterion = Restrictions.like(stes[0], v);
                            BaseDao.this.criteria.add(criterion);
                            BaseDao.this.criterionList.add(criterion);
                        }

                        break;
                    case "eq":
                        if (v != null) {
                            criterion = Restrictions.eq(stes[0], v);
                            BaseDao.this.criteria.add(criterion);
                            BaseDao.this.criterionList.add(criterion);
                        }
                        break;
                    case "gt":
                        if (v != null) {
                            criterion = Restrictions.gt(stes[0], v);
                            BaseDao.this.criteria.add(criterion);
                            BaseDao.this.criterionList.add(criterion);
                        }
                        break;
                    case "ge":
                        if (v != null) {
                            criterion = Restrictions.ge(stes[0], v);
                            BaseDao.this.criteria.add(criterion);
                            BaseDao.this.criterionList.add(criterion);
                        }
                        break;
                    case "le":
                        if (v != null) {
                            criterion = Restrictions.le(stes[0], v);
                            BaseDao.this.criteria.add(criterion);
                            BaseDao.this.criterionList.add(criterion);
                        }
                        break;
                    case "lt":
                        if (v != null) {
                            criterion = Restrictions.lt(stes[0], v);
                            BaseDao.this.criteria.add(criterion);
                            BaseDao.this.criterionList.add(criterion);
                        }
                        break;
                    case "in":
                        if (v != null) {
                            var list = (List) v;
                            if (list.size() > 0) {
                                criterion = Restrictions.in(stes[0], v);
                                BaseDao.this.criteria.add(criterion);
                                BaseDao.this.criterionList.add(criterion);
                            }
                        }

                        break;
                    case "notIn":
                        if (v != null) {
                            var list = (List) v;
                            if (list.size() > 0) {
                                Criterion in = Restrictions.in(stes[0], v);
                                criterion = Restrictions.not(in);
                                BaseDao.this.criteria.add(criterion);
                                BaseDao.this.criterionList.add(criterion);
                            }
                        }
                        break;

                }
            }

        }
        System.out.println("initwhere");
        return criterion;
    }

    //初始化or条件
    private void orWhere() {
        this.orPageData.forEach(
                p -> {
                    initWhere(p);
                    var or = Restrictions.or(this.criterionList.toArray(Criterion[]::new));
                    BaseDao.this.criteria.add(or);
                }
        );
        System.out.println("orwhere");
    }


    /**
     * 初始化查询字断
     */
    private void initSelect() {
        this.projectionList = Projections.projectionList();
        List<String> fileds = new ArrayList<>();
        if (this.isT) {
            fileds = getSelectFileds(this.tClass);
        } else {
            fileds = getSelectFileds(this.dtoClass);
        }
        fileds.forEach(
                filed -> {
                    if (BaseDao.this.groupFileds.toString().contains(filed)) {
                        BaseDao.this.projectionList.add(Projections.groupProperty(filed).as(getAlias(filed)));
                    } else {
                        BaseDao.this.projectionList.add(Projections.property(filed).as(getAlias(filed)));
                    }
                }
        );
        this.criteria.setProjection(BaseDao.this.projectionList);
        System.out.println("initselect");
    }

    /**
     * 获取需要查询的字断
     *
     * @param clazz
     * @return
     */
    private List<String> getSelectFileds(Class clazz) {
        List<String> fields = new ArrayList<>();
        //如果有自定义的查询字断则直接返回自定义的
        if (null != BaseDao.this.selectFileds && BaseDao.this.selectFileds.length() > 0) {
            String[] strs = this.selectFileds.split(",");
            Collections.addAll(fields, strs);
            return fields;
        }
        //没有的话则根据类型来获取
        Field[] declaredFields = clazz.getDeclaredFields();
        Field[] superClassFields = clazz.getSuperclass().getDeclaredFields();
        List<Field> list = new ArrayList<>();
        if (null != superClassFields && superClassFields.length > 0) {
            Collections.addAll(list, superClassFields);
        }
        Collections.addAll(list, declaredFields);
        list.forEach(
                field -> {
                    Ignore annotation = field.getAnnotation(Ignore.class);
                    if (null == annotation) {
                        var fieldStr = field.getName();
                        fields.add(getFields(fieldStr));
                    }
                }
        );

        return fields;
    }

    /**
     * 获取需要查询的字断并判断需不需要连表
     *
     * @param filedName
     * @return
     */
    private String getFields(String filedName) {
        for (var str : BaseDao.this.joinTableName) {
            int index = filedName.indexOf(str);
            if (index == 0) {
                String substring = filedName.substring(str.length());
                substring = MyStringUtils.lowerCase(substring);
                String s = str + "." + substring;
                return s;
            }
        }
        return filedName;
    }

    /**
     * 根据查询字断获取别名
     *
     * @param str
     * @return
     */
    private String getAlias(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        boolean flag = false;
        for (var c : chars) {
            if (c == '.') {
                flag = true;
            } else {
                if (flag) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
                flag = false;
            }

        }
        return sb.toString();
    }



}
