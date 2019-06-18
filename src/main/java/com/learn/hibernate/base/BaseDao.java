package com.learn.hibernate.base;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import com.learn.hibernate.domian.DtoOrT;
import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.domian.PageInfo;
import com.learn.hibernate.entity.Action;
import com.learn.hibernate.utils.MyStringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
import java.lang.reflect.*;
import java.util.*;

/**
 * @param <T>
 * @param <DTO>
 * @param <D>
 * @author lee
 */
@Data
@AllArgsConstructor
@Component
@PropertySource("classpath:my_base_dao.properties")
@SuppressWarnings({"unused", "unchecked", "rawtypes", "null", "hiding"})
@Slf4j
public class BaseDao<T, DTO, D> {

    @Value("${mybasedao.entity}")
    private String entityString;

    @Value("${mybasedao.dto}")
    private String dtoString;

    @Value("${mybasedao.dto.suffix}")
    private String dtoSuffix;

    private Root joinRoot;

    private String entityName;

    CriteriaBuilder cb;

    CriteriaQuery cq;

    boolean isGroup;

    Root root;

    Class<T> tClass;

    Class<DTO> dtoClass;


    private PageData orPageData;

    @Autowired
    private BaseQuery baseQuery;


    public BaseDao() {
    }

    public void init(String clazz) throws ClassNotFoundException {
        String dtoName = clazz + dtoSuffix;
        this.entityName = clazz;
        initTClz(clazz);
        initDtoClz(dtoName);
        this.baseQuery = baseQuery.init(this.tClass);
        initBQR();
    }

    public void init(Class<T> clazz) throws ClassNotFoundException {
        this.tClass = clazz;
        var tableName = MyStringUtils.subStringLastChar(clazz.getName(), '.');
        this.entityName = tableName;
        String dtoName = tableName + dtoSuffix;
        initDtoClz(dtoName);
        this.baseQuery = baseQuery.init(this.tClass);
        initBQR();
    }

    public void initTClz(String clazz) throws ClassNotFoundException {
        var packagelist = Arrays.asList(this.entityString.split(","));
        int count = 0;
        for (String str : packagelist) {
            try {
                this.tClass = (Class<T>) Class.forName(str + "." + clazz);
            } catch (ClassNotFoundException e) {
                count++;
                if (packagelist.size() <= count) {
                    throw e;
                }
            }
        }
    }

    public Class getMyClass(String clazz) throws ClassNotFoundException {
        var packagelist = Arrays.asList(this.entityString.split(","));
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

    public void initDtoClz(String dtoName) throws ClassNotFoundException {
        int count = 0;
        var packagelist = Arrays.asList(this.dtoString.split(","));
        for (String str : packagelist) {
            try {
                this.dtoClass = (Class<DTO>) Class.forName(str + "." + dtoName);
            } catch (ClassNotFoundException e) {
                log.error("-----------------------------------------------" + dtoName + "未找到相应的dto类");
            }
        }
    }

    public void init(Class<T> entityClz, Class<DTO> dtoClz) {
        this.entityName = MyStringUtils.subStringLastChar(entityClz.getName(), '.');
        this.tClass = entityClz;
        this.dtoClass = dtoClz;
        this.baseQuery = baseQuery.init(this.tClass);
        initBQR();
    }


    private void initBQR() {
        this.cb = this.baseQuery.getSession().getCriteriaBuilder();
        this.cq = this.cb.createTupleQuery();
        this.root = this.cq.from(tClass);
    }


    public Query getQuery() {
        return this.baseQuery.getSession().createQuery(cq);
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
        try {
            T t = null;
            this.getBaseQuery().getSession().beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                t = list.get(i);
                this.baseQuery.getSession().saveOrUpdate(t);
                if (i % 50 == 0) {
                    this.getBaseQuery().getSession().flush();
                    this.getBaseQuery().getSession().clear();
                }
            }
            this.getBaseQuery().getSession().getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误信息
            this.getBaseQuery().getSession().getTransaction().rollback();
        } finally {
            this.getBaseQuery().getSession().close();
        }
        return true;
    }

    public Boolean updateById(T t, D id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        var result = this.baseQuery.asUpdate().set(t).where().eq("id", id).updateExecution();
        return result > 0;
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
        return this.baseQuery.where().eq("id", id).asUpdate().set("isDelete", 1).updateExecution();
    }

    protected PageData putId(D id, PageData pageData) {
        PageData myPageData = new PageData();
        myPageData.put("id_eq", id);
        if (pageData != null) {
            myPageData.putAll(pageData.getMap());
        }
        return myPageData;
    }


    public DTO getInfoDto(D id, PageData pageData, String... fields) throws ClassNotFoundException {
        var query = getInfoQuery(false, putId(id, pageData), fields);
        Tuple result = (Tuple) query.getSingleResult();
        return getDto(result);
    }


    public DTO getInfoDto(D id, String... fields) throws ClassNotFoundException {
        var query = getInfoQuery(false, null, fields);
        Tuple result = (Tuple) query.getSingleResult();
        return getDto(result);
    }


    public DTO getInfoDto(D id, String fields) throws ClassNotFoundException {
        return getInfoDto(id, fields.split(","));
    }


    public DtoOrT getInfoDtoOrT(D id, boolean isT, PageData pageData) throws ClassNotFoundException {
        var query = getInfoQuery(isT, putId(id, pageData));
        Tuple result = (Tuple) query.uniqueResult();
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
        var query = getInfoQuery(isT, putId(id, null));
        Tuple result = (Tuple) query.getSingleResult();
        DtoOrT<DTO, T> dtoOrT = new DtoOrT<DTO, T>();
        if (isT) {
            dtoOrT.setT(getT(result));
        } else {
            dtoOrT.setDto(getDto(result));
        }
        return dtoOrT;
    }

    private Predicate[] getPredicateArray(PageData pageData) {
        List<Predicate> list = getPredicates(pageData);
        if (this.orPageData != null && this.orPageData.size() > 0) {
            var orPredicates = getPredicates(this.orPageData);
            list.add(this.cb.or(orPredicates.toArray(Predicate[]::new)));
        }
        return list.toArray(Predicate[]::new);
    }

    public Query getInfoQuery(boolean isT, PageData pageData, String... fileds) throws ClassNotFoundException {
        Predicate[] predicates = getPredicateArray(pageData);

        if (predicates.length > 0) {
            this.getCq().where(predicates);
        }
        if (fileds.length <= 0) {
            selectFilds(isT);
        } else {
            selectFilds(fileds);
        }
        var query = getQuery();
        return query;
    }

    /**
     * @param pageData
     * @param isT
     * @return
     * @throws ClassNotFoundException
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

    public DtoOrT getDtoOrTList(PageData pageData, boolean isT,String... fileds) throws ClassNotFoundException {
        var query = getListQuery(pageData, isT,fileds);
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
        var query = getListQuery(pageData, isT);
        PageInfo pageInfo = new PageInfo(pageData, new MyBQR(cb, cq, root), m -> getQuery(), this.isGroup);
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
        var query = getListQuery(pageData, isT, fileds);
        PageInfo pageInfo = new PageInfo(pageData, new MyBQR(cb, cq, root), m -> getQuery(), this.isGroup);
        query.setFirstResult(pageData.getMaxRows());
        query.setMaxResults(pageInfo.getPageSize());
        var result = getDtoList(query.getResultList());
        pageInfo.setList(result);
        return pageInfo;
    }


    public Query getListQuery(PageData pageData, boolean isT, String... fileds) {
        var p = getPredicateArray(pageData);
        if(null != p && p.length >0){
            cq.where(p);
        }
        if (fileds == null || fileds.length <= 0) {
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
        if (null == this.dtoClass) {
            throw new RuntimeException("未找到相应的dto");
        }
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
        if (tuple == null) {
            return map;
        }
        for (int i = 0; i < tuple.getElements().size(); i++) {
            var object = tuple.getElements();
            var alias = object.get(i).getAlias();
            var value = tuple.get(i);
            map.put(alias, value);
        }
        return map;
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

    public Map<String, Boolean> getFildsName(Field[]... fields) {
        Map<String, Boolean> fildsName = new HashMap<>();
        if (fields.length <= 0) {
            return fildsName;
        }
        var fieldss = Arrays.asList(fields);
        fieldss.forEach(
                fs -> {
                    Arrays.asList(fs).forEach(
                            f -> {
                                var ignore = f.getAnnotation(Ignore.class);
                                if (null == ignore) {
                                    fildsName.put(f.getName(), getIsJoin(f));
                                } else {
                                    if (!ignore.value()) {
                                        fildsName.put(f.getName(), getIsJoin(f));
                                    }
                                }

                            }
                    );
                }
        );

        return fildsName;
    }


    /**
     * 获取是否需要自动关联表
     *
     * @param field
     * @return
     */
    private boolean getIsJoin(Field field) {
        var noJoin = field.getAnnotation(Nojoin.class);
        if (null == noJoin) {
            return false;
        } else {
            return noJoin.value();
        }
    }

    /**
     * 初始化需要查询的字段
     *
     * @param isT
     */
    public void selectFilds(boolean isT) {
        List<Selection> selections = new ArrayList<>();
        Field[] fields = null;
        Map<String, Boolean> fildsNames = null;
        if (isT) {
            var childFields = tClass.getDeclaredFields();
            var superTClassFields = tClass.getSuperclass().getDeclaredFields();
            fildsNames = getFildsName(childFields, superTClassFields);
        } else {
            fields = dtoClass.getDeclaredFields();
            fildsNames = getFildsName(fields);
        }
        fildsNames.forEach(
                (k, v) -> {
                    Selection selection = null;
                    var strs = getFildsUpChar(k, v);
                    if (strs.length == 1) {
                        selection = this.root.get(k).alias(k);
                        selections.add(selection);
                    } else {
                        selection = getSelection(strs);
                        selections.add(selection);
                    }
                }
        );
        cq.multiselect(selections);
    }

    /**
     * 设置排序
     *
     * @param fields
     */
    public void setOrderBy(String... fields) {
        List<Order> orders = new ArrayList<>();
        if (fields.length > 0) {
            var fieldList = Arrays.asList(fields);
            for (String str : fieldList) {
                var strs = str.split(",");
                if (strs.length > 1) {
                    switch (strs[1]) {
                        case "asc":
                            orders.add(this.cb.asc(getPath(strs[0])));
                            break;
                        case "desc":
                            orders.add(this.cb.desc(getPath(strs[0])));
                            break;
                    }
                }
            }

        }
        this.cq.orderBy(orders);
    }

    /**
     * 设置groupBy
     *
     * @param fields
     */
    public void setGroupBy(String... fields) {
        List<Path> paths = new ArrayList<>();
        Arrays.asList(fields).forEach(
                f -> {
                    var p = getPath(f);
                    paths.add(p);
                }
        );
        this.cq.groupBy(paths);
        this.isGroup = true;
    }


    public String[] getFildsUpChar(String str, Boolean ignore) {
        StringBuilder sb = new StringBuilder();
        if (ignore) {
            return str.split("\\.");
        }
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

    public Path getPath(String... strings) {
        switch (strings.length) {
            case 1:
                return this.root.get(strings[0]);
            case 2:
                return this.root.get(strings[0]).get(strings[1]);
            case 3:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]);
            case 4:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]).get(strings[3]);
            case 5:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]).get(strings[3]).get(strings[4]);
            case 6:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]).get(strings[3]).get(strings[4]).get(strings[5]);
            case 7:
                return this.root.get(strings[0]).get(strings[1]).get(strings[2]).get(strings[3]).get(strings[4]).get(strings[5]).get(strings[6]);
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
        Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
        if(null != declaredFields && declaredFields.length > 0) {
            setFiledValue(map,obj,declaredFields);
        }
        if(null != superFields && superFields.length > 0) {
            setFiledValue(map,obj,superFields);
        }

        return obj;
    }

    private void setFiledValue(Map<String, Object> map,Object obj, Field[] fields) {
        for (Field field : fields) {
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
    }


    public static void main(String[] args) {
        Action action = new Action();
        var a = action.getClass().getFields();

        String[] str = new String[]{"ddd","fff"};
        String[] str1 = new String[]{"GGG","dddde"};
        List<String> b = Arrays.asList(str);
        List<String> c = Arrays.asList(str1);
        b.addAll(c);
        System.out.println(b);
    }


    public List<Predicate> getPredicates(PageData pageData) {
        List<Predicate> ps = new ArrayList<>();
        if (pageData == null) {
            return ps;
        }
        pageData.getMap().forEach(
                (k, v) -> {
                    var stes = k.split("_");
                    if (stes.length > 1) {
                        switch (stes[1]) {
                            case "like":
                                if (v != null && !"".equals(v.toString().trim())) {
                                    ps.add(getPredicateLike(stes[0], v.toString()));
                                }
                                break;
                            case "eq":
                                if (v != null) {
                                    ps.add(getPredicateEq(stes[0], v));
                                }
                                break;
                            case "gt":
                                if (v != null) {
                                    ps.add(getPredicateGt(stes[0], (Number) v));
                                }
                                break;
                            case "ge":
                                if (v != null) {
                                    ps.add(getPredicateGt(stes[0], (Number) v));
                                }
                                break;
                            case "le":
                                if (v != null) {
                                    ps.add(getPredicateGt(stes[0], (Number) v));
                                }
                                break;
                            case "lt":
                                if (v != null) {
                                    ps.add(getPredicateGt(stes[0], (Number) v));
                                }
                                break;
                            case "in":
                                if (v != null) {
                                    var list = (List) v;
                                    if (list.size() > 0) {
                                        ps.add(getPredicateIn(stes[0], (List) v));
                                    }
                                }

                                break;
                            case "notIn":
                                if (v != null) {
                                    var list = (List) v;
                                    if (list.size() > 0) {
                                        ps.add(getPredicateIn(stes[0], (List) v));
                                    }
                                }
                                break;

                        }
                    }

                }
        );
        return ps;
    }


    public Predicate getPredicateLike(String fildName, String value) {
        return getCb().like(getPath(fildName), value);
    }

    public Predicate getPredicateEq(String fildName, Object value) {
        return getCb().equal(getPath(fildName), value);
    }


    public Predicate getPredicateGe(String fildName, Number value) {
        return getCb().ge(getPath(fildName), value);
    }

    public Predicate getPredicateGt(String fildName, Number value) {
        return getCb().gt(getPath(fildName), value);
    }

    public Predicate getPredicateLe(String fildName, Number value) {
        return getCb().le(getPath(fildName), value);
    }

    public Predicate getPredicateLt(String fildName, Number value) {
        return getCb().lt(getPath(fildName), value);
    }

    public Predicate getPredicateIn(String fildName, List value) {
        var in = getCb().in(getPath(fildName));
        value.forEach(in::value);
        return in;
    }

    public Predicate getPredicateNotIn(String fildName, List value) {
        var in = getCb().in(getPath(fildName));
        value.forEach(in::value);
        var notIn = getCb().not(in);
        return notIn;
    }

    public Path getPath(String filedName) {
        Path path = null;
        if (filedName.contains(".")) {
            var strs = filedName.split("\\.");
            path = getPath(strs);
        } else {
            var strs = filedName.split("\\.");
            path = getPath(strs);
        }
        return path;
    }


    public String getAlisdName(String name) {
        if (name.contains(".")) {
            StringBuilder stringBuilder = new StringBuilder();
            boolean flag = false;
            for (char b : name.toCharArray()) {
                if (b == '.') {
                    flag = true;
                } else {
                    if (flag) {
                        stringBuilder.append(String.valueOf(b).toUpperCase());
                        flag = false;
                    } else {
                        stringBuilder.append(b);
                    }
                }
            }
            return stringBuilder.toString();
        } else {
            return name;
        }
    }


}
