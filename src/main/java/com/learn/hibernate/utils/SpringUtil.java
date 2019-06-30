package com.learn.hibernate.utils;

import org.springframework.context.ApplicationContext;

/**
 * TODO
 * email leeoohoo@gmail.com
 * rocketmq
 * Created by lee on 2019-06-28.
 */
public class SpringUtil {
    private static ApplicationContext context = null;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }
}
