package com.learn.hibernate.base;

import com.learn.hibernate.domian.DtoOrT;
import com.learn.hibernate.domian.JoinRoot;
import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.domian.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author lee
 * @param <T>
 * @param <DTO>
 * @param <D>
 */
@Data
@AllArgsConstructor
@Component
@PropertySource("classpath:my_base_dao.properties")
public class BaseDao<T, DTO, D>  {

    @Value("${mybasedao.entity}")
    private String entityString;

    @Value("${mybasedao.dto}")
    private String dtoString;

    @Value("${mybasedao.dto.suffix}")
    private String dtoSuffix;

    private Root joinRoot;

    CriteriaBuilder cb;

    CriteriaQuery cq;

    Root root;

    Class<T> tClass;

    Class<DTO> dtoClass;

    @Autowired
    private EntityManager entityManager;

    public Session getSession() {
        return getEntityManager().unwrap(Session.class);
    }

    public BaseDao(Class<T> tClass, Class<DTO> dtoClass) {
        this.tClass = tClass;
        this.dtoClass = dtoClass;
        getBQR();
    }


    public BaseDao() {
    }

    public void init(String clazz) throws ClassNotFoundException {
        String dtoName = clazz + dtoSuffix;
        this.tClass = (Class<T>) Class.forName(this.entityString + clazz);
        this.dtoClass = (Class<DTO>) Class.forName(this.dtoString + dtoName);
        getBQR();
    }



    private void getBQR() {
        this.cb = getSession().getCriteriaBuilder();
        this.cq = this.cb.createTupleQuery();
        this.root = this.cq.from(tClass);
    }


    public Query getQuery() {
        return getSession().createQuery(cq);
    }

    public D add(T t)  {
        return (D) getSession().save(t);
    }

    public T update(T t)  {
        return (T) getSession().merge(t);
    }

    public T delete(D id, boolean isDeleted) throws IllegalAccessException, IntrospectionException, InvocationTargetException, ClassNotFoundException {
        T t = (T) getInfoDtoOrT(id,true).getT();
        if (t == null) {
            return null;
        }
        if (isDeleted) {
            return deleteByIsDeleted(t);
        }
        getSession().delete(t);
        return t;
    }

    @Transactional
    public boolean deleteBach(String... ids) throws IllegalAccessException, IntrospectionException, InvocationTargetException, ClassNotFoundException {
        var list = Arrays.asList(ids);

        for (String id : list) {
            var result = delete((D) id, false);
            if (result == null) {
                throw new RuntimeException();
            }
        }
        return true;
    }


    @Transactional
    public boolean deleteByIsDeletedBach(String... ids) throws IllegalAccessException, IntrospectionException, InvocationTargetException, ClassNotFoundException {
        var list = Arrays.asList(ids);
        for (String id : list) {
            var result = delete((D) id, true);
            if (result == null) {
                throw new RuntimeException();
            }
        }
        return true;
    }


    public T deleteByIsDeleted(T t) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if (hasCloums(t, "isDeleted")) {
            t = setProperty(t, "isDeleted", 1);
            getSession().merge(t);
        }
        return t;
    }




    public DTO getInfoDto(D id, PageData pageData, String... fields) throws ClassNotFoundException {
        var query = getInfoQuery(id, false, pageData, fields);
        Tuple result = (Tuple) query.getSingleResult();
        return getDto(result);
    }

    public DTO getInfoDto(D id, String... fields) throws ClassNotFoundException {
        var query = getInfoQuery(id, false, null, fields);
        Tuple result = (Tuple) query.getSingleResult();
        return getDto(result);
    }

    public DtoOrT getInfoDtoOrT(D id, boolean isT, PageData pageData) throws ClassNotFoundException {
        var query = getInfoQuery(id, isT, pageData);
        Tuple result = (Tuple) query.getSingleResult();
        DtoOrT<DTO, T> dtoOrT = new DtoOrT<DTO, T>();
        if (isT) {
            dtoOrT.setT(getT(result));
        } else {
            dtoOrT.setDto(getDto(result));
        }
        return dtoOrT;
    }

    /**
     * 没有条件的getInfo
     *
     * @param id
     * @param isT
     * @return
     */
    public DtoOrT getInfoDtoOrT(D id, boolean isT) throws ClassNotFoundException {
        var query = getInfoQuery(id, isT, null);
        Tuple result = (Tuple) query.getSingleResult();
        DtoOrT<DTO, T> dtoOrT = new DtoOrT<DTO, T>();
        if (isT) {
            dtoOrT.setT(getT(result));
        } else {
            dtoOrT.setDto(getDto(result));
        }
        return dtoOrT;
    }

    public Query getInfoQuery(D id, boolean isT, PageData pageData, String... fileds) throws ClassNotFoundException {
        PageData myPageData = new PageData();
        myPageData.put("id_eq", id);
        if (pageData != null) {
            myPageData.putAll(pageData.getMap());
        }
        List<Predicate> list = getPredicates(myPageData);
        cq.where(list.toArray(Predicate[]::new));
        if (fileds.length <= 0) {
            selectFilds(isT);
        } else {
            selectFilds(fileds);
        }
        var query = getQuery();
        return query;
    }

    /**
     * 获取列表
     *
     * @param pageData
     * @return
     */
    public DtoOrT getDtoOrTList(PageData pageData, boolean isT) throws ClassNotFoundException {
        var query = getListQuery(pageData, isT);
        List<Tuple> result = query.getResultList();
        DtoOrT<DTO, T> dtoOrT = new DtoOrT<DTO, T>();
        if (isT) {
            dtoOrT.setTList(getList(result));
        } else {
            dtoOrT.setDtoList(getDtoList(result));
        }
        return dtoOrT;
    }

    /**
     * 获取DTO列表
     *
     * @param pageData
     * @return
     */
    public List<DTO> getDtoList(PageData pageData, String... fileds) throws ClassNotFoundException {
        var query = getListQuery(pageData, false, fileds);
        List<Tuple> result = query.getResultList();
        return getDtoList(result);
    }


    /**
     * 获取分页信息
     *
     * @param pageData
     * @return
     */
    public PageInfo getPageInfo(PageData pageData, boolean isT) throws ClassNotFoundException {
        PageInfo pageInfo = new PageInfo(pageData, new MyBQR(cb, cq, root), m -> getQuery());
        var query = getListQuery(pageData, isT);
        query.setFirstResult(pageData.getMaxRows());
        query.setMaxResults(pageInfo.getPageSize());
        var result = getDtoList(query.getResultList());
        pageInfo.setList(result);
        return pageInfo;
    }

    /**
     * 获取分页信息
     *
     * @param pageData
     * @param fileds   需要查询的字断
     * @return
     */
    public PageInfo getPageInfo(PageData pageData, boolean isT, String... fileds) throws ClassNotFoundException {
        PageInfo pageInfo = new PageInfo(pageData, new MyBQR(cb, cq, root), m -> getQuery());
        var query = getListQuery(pageData, isT, fileds);
        query.setFirstResult(pageData.getMaxRows());
        query.setMaxResults(pageInfo.getPageSize());
        var result = getDtoList(query.getResultList());
        return pageInfo;
    }


    public Query getListQuery(PageData pageData, boolean isT, String... fileds) throws ClassNotFoundException {
        var p = getPredicates(pageData).toArray(Predicate[]::new);
        cq.where(p);
        if (fileds.length <= 0) {
            selectFilds(isT);
        } else {
            selectFilds(fileds);
        }
        var query = getQuery();
        return query;
    }


    /**
     * 获取DTO
     *
     * @param list
     * @return
     */
    public List<DTO> getDtoList(List<Tuple> list) {
        List<DTO> result = new ArrayList<>();
        list.forEach(
                r -> {
                    DTO dto = getDto(r);
                    result.add(dto);
                }
        );
        return result;
    }

    /**
     * 获取DTO
     *
     * @param list
     * @return
     */
    public List<T> getList(List<Tuple> list) {
        List<T> result = new ArrayList<>();
        list.forEach(
                r -> {
                    T t = getT(r);
                    result.add(t);
                }
        );
        return result;
    }

    public T getT(Tuple tuple) {
        return (T) map2Obj(getResultMap(tuple), tClass);
    }

    public DTO getDto(Tuple tuple) {
        return (DTO) map2Obj(getResultMap(tuple), dtoClass);
    }

    /**
     * 根据结果中某一个元素获取对象map
     *
     * @param tuple
     * @return
     */
    public Map<String, Object> getResultMap(Tuple tuple) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < tuple.getElements().size(); i++) {
            var object = tuple.getElements();
            var alias = object.get(i).getAlias();
            var value = tuple.get(i);
            map.put(alias, value);
        }
        return map;
    }

    public List<Selection> getSelections(Root root) {
        List<Selection> list = new ArrayList<>();
        var dtoClz = getDtoClass();
        var fields = dtoClz.getDeclaredFields();
        Arrays.asList(fields).forEach(
                f -> {
                    System.out.println(f.getName());
                    list.add(root.get(f.getName()).alias(f.getName()));
                }
        );
        return list;
    }

    /**
     * 初始化查询字断
     *
     * @param fileds
     */
    public void selectFilds(String... fileds) {
        List<Selection> selections = new ArrayList<>();
        Arrays.asList(fileds).forEach(
                f -> {
                    if (f.contains(".")) {
                        var strs = f.split("\\.");
                        selections.add(getSelection(strs));
                    } else {
                        selections.add(this.root.get(f).alias(f));
                    }
                }
        );
        cq.multiselect(selections);
    }

    public void selectFilds(boolean isT) {
        List<Selection> selections = new ArrayList<>();
        Field[] fields = null;
        if (isT) {
            fields = tClass.getDeclaredFields();
        } else {
            fields = dtoClass.getDeclaredFields();
        }
        Arrays.asList(fields).forEach(
                f -> {
                    Selection selection = null;
                    var strs = getFildsUpChar(f.getName());
                    if (strs.length == 1) {
                        selection = this.root.get(f.getName()).alias(f.getName());
                        selections.add(selection);
                    } else {
                        selection = getSelection(strs);
                        selections.add(selection);
                    }
                }
        );
        cq.multiselect(selections);
    }


    public String[] getFildsUpChar(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                sb.append("." + String.valueOf(c).toLowerCase());
            } else {
                sb.append(String.valueOf(c));
            }
        }

        return sb.toString().split("\\.");
    }

    /**
     * 获取查询字断
     *
     * @param strings
     * @return
     */
    public Selection getSelection(String... strings) {
        switch (strings.length) {
            case 2:
                return this.root.get(strings[0]).get(strings[1]).alias(getAlias(strings));
            case 3:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]).alias(getAlias(strings));
            case 4:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]).get(strings[3]).alias(getAlias(strings));
            case 5:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]).get(strings[3]).get(strings[4]).alias(getAlias(strings));
            case 6:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]).get(strings[3]).get(strings[4]).get(strings[5]).alias(getAlias(strings));
            case 7:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]).get(strings[3]).get(strings[4]).get(strings[5]).get(strings[6]).alias(getAlias(strings));
        }
        return null;
    }

    public String getAlias(String... strings) {
        var alias = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            var str = strings[i];
            if (i == 0) {
                alias.append(str);
            } else {
                alias.append(oneCharsToUp(str));
            }
        }
        return alias.toString();
    }

    /**
     * 将第一个字母变大写
     *
     * @param str
     * @return
     */
    public String oneCharsToUp(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.replace(0, 1, String.valueOf(sb.charAt(0)).toUpperCase());
        return sb.toString();
    }


    public Object map2Obj(Map<String, Object> map, Class<?> clz) {
        Object obj = null;
        try {
            obj = clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(obj, map.get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }



    public List<Predicate> getPredicates(PageData pageData) throws ClassNotFoundException {
        List<Predicate> ps = new ArrayList<>();

        for (Map.Entry entry : pageData.getMap().entrySet()){
            var stes = entry.getKey().toString().split("_");
            switch (stes[1]) {
                case "like":
                    ps.add(getPredicateLike(stes[0], entry.getValue().toString()));
                    break;
                case "eq":
                    ps.add(getPredicateEq(stes[0], entry.getValue()));
                    break;
                case "gt":
                    ps.add(getPredicateGt(stes[0], (Number) entry.getValue()));
                    break;
                case "ge":
                    ps.add(getPredicateGe(stes[0], (Number) entry.getValue()));
                    break;
                case "le":
                    ps.add(getPredicateLe(stes[0], (Number) entry.getValue()));
                    break;
                case "lt":
                    ps.add(getPredicateLt(stes[0], (Number) entry.getValue()));
                    break;
            }
        }

        return ps;
    }


    public Predicate getPredicateLike( String fildName, String value) throws ClassNotFoundException {
        var joinRoot = getRoot(fildName);
        return getCb().like(joinRoot.getRoot().get(joinRoot.getFiled()), value);
    }

    public Predicate getPredicateEq(String fildName, Object value) throws ClassNotFoundException {
        var joinRoot = getRoot(fildName);
        return getCb().equal(joinRoot.getRoot().get(joinRoot.getFiled()), value);
    }

    public Predicate getPredicateGe(String fildName, Number value) throws ClassNotFoundException {
        var joinRoot = getRoot(fildName);
        return getCb().ge(joinRoot.getRoot().get(joinRoot.getFiled()), value);
    }

    public Predicate getPredicateGt(String fildName, Number value) throws ClassNotFoundException {
        var joinRoot = getRoot(fildName);
        return getCb().gt(joinRoot.getRoot().get(joinRoot.getFiled()), value);
    }

    public Predicate getPredicateLe(String fildName, Number value) throws ClassNotFoundException {
        var joinRoot = getRoot(fildName);
        return getCb().le(joinRoot.getRoot().get(joinRoot.getFiled()), value);
    }

    public Predicate getPredicateLt(String fildName, Number value) throws ClassNotFoundException {
        var joinRoot = getRoot(fildName);
        return getCb().lt(joinRoot.getRoot().get(joinRoot.getFiled()), value);
    }

    public Predicate getPredicateIn(String fildName, List value) throws ClassNotFoundException {
        var joinRoot = getRoot(fildName);
        var in = getCb().in(joinRoot.getRoot().get(joinRoot.getFiled()));
        value.forEach(in::value);
        return in;
    }

    public JoinRoot getRoot(String filedName) throws ClassNotFoundException {
        JoinRoot joinRoot = new JoinRoot();
        if(filedName.contains(".")) {
            var strs = filedName.split("\\.");
            Class clz = Class.forName(this.entityString + strs[0]);
            Root root = getCq().from( clz);
            joinRoot.setFiled(strs[1]);
            joinRoot.setRoot(root);
        }else {
            joinRoot.setRoot(getRoot());
            joinRoot.setFiled(filedName);
        }
        return joinRoot;
    }


    public String getAlisdName(String name) {
        if(name.contains(".")) {
            StringBuilder stringBuilder = new StringBuilder();
            boolean flag = false;
            for (char b : name.toCharArray()) {
                if(b == '.'){
                    flag = true;
                }else {
                    if(flag){
                        stringBuilder.append(String.valueOf(b).toUpperCase());
                        flag = false;
                    }else {
                        stringBuilder.append(b);
                    }
                }
            }
            return stringBuilder.toString();
        }else {
            return name;
        }
    }


    /*该方法用于传入某实例对象以及对象方法名、修改值，通过放射调用该对象的某个set方法设置修改值*/
    public T setProperty(T beanObj, String property, Object value) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        //此处应该判断beanObj,property不为null
        PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
        Method setMethod = pd.getWriteMethod();
        if (setMethod == null) {

        }
        assert setMethod != null;
        return (T) setMethod.invoke(beanObj, value);
    }

    /*该方法用于传入某实例对象以及对象方法名，通过反射调用该对象的某个get方法*/
    public Object getProperty(T beanObj, String property) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        //此处应该判断beanObj,property不为null
        PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
        Method getMethod = pd.getReadMethod();
        if (getMethod == null) {

        }
        assert getMethod != null;
        return getMethod.invoke(beanObj);
    }

    //判断列是否存
    public <T> boolean hasCloums(T beanObj, String property) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
            Method getMethod = pd.getReadMethod();
            if (getMethod == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
