package com.learn.hibernate.utils;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;

public class ClassUtils {


    /*该方法用于传入某实例对象以及对象方法名、修改值，通过放射调用该对象的某个set方法设置修改值*/
    public static synchronized  <T> Object setProperty(T beanObj, String property, Object value) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        //此处应该判断beanObj,property不为null
        PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
        Method setMethod = pd.getWriteMethod();
        if (setMethod == null) {

        }
        assert setMethod != null;
        return setMethod.invoke(beanObj, value);
    }

    /*该方法用于传入某实例对象以及对象方法名、修改值，通过放射调用该对象的某个set方法设置修改值*/
    public static synchronized  <T> Object setProperty(T beanObj, String property, Object value, Class clazz) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        //此处应该判断beanObj,property不为null
        PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
        Method setMethod = pd.getWriteMethod();
        if (setMethod == null) {

        }
        assert setMethod != null;
        if(clazz.getName().contains("Long")) {
            value = Long.parseLong(value.toString());
        }else if(clazz.getName().contains("BigInteger")) {
            value = BigInteger.valueOf((Long) value);
        }

        return setMethod.invoke(beanObj, value);
    }

    /*该方法用于传入某实例对象以及对象方法名，通过反射调用该对象的某个get方法*/
    public static synchronized  <T> Object getProperty(T beanObj, String property) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        //此处应该判断beanObj,property不为null
        PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
        Method getMethod = pd.getReadMethod();
        if (getMethod == null) {

        }
        assert getMethod != null;
        return getMethod.invoke(beanObj);
    }

    //判断列是否存
    public static synchronized  <T> boolean hasCloums(T beanObj, String property) {
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


    /**
     * 将对象中不为null的字断转换为map
     * @param t
     * @return
     * @throws IllegalAccessException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     */
    public static synchronized Map<String, Object> tToMap(Object t) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map<String, Object> map = new HashMap<>();
        var childFields = t.getClass().getDeclaredFields();
        var superTClassFields = t.getClass().getSuperclass().getDeclaredFields();
        List<String> fields = getFildsName(childFields,superTClassFields);
        for(String filed : fields) {
            var value = ClassUtils.getProperty(t,filed);
            if(value != null) {
                map.put(filed,value);
            }
        }
        return map;
    }

    public static List<String> getFildsName(Field[]... fields) {
        List<String> fildsName = new ArrayList<>();
        if (fields.length <= 0) {
            return fildsName;
        }
        var fieldss = Arrays.asList(fields);
        fieldss.forEach(
                fs -> {
                    Arrays.asList(fs).forEach(
                            f -> {
                                fildsName.add(f.getName());
                            }
                    );
                }
        );

        return fildsName;
    }




}
