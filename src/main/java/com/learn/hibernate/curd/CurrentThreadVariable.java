package com.learn.hibernate.curd;

import org.springframework.stereotype.Component;

/**
 * 当前线程的变量<integer></>
 * email leeoohoo@gmail.com
 * system
 * Created by lee on 2019/10/11.
 */
@Component
public class CurrentThreadVariable {

    private static final ThreadLocal<Integer> userHolder = new ThreadLocal<Integer>();

    /**
     * 设置当前线程的变量
     * @param integer
     */
    public static void setCurrentThreadVariable(Integer integer) {
        userHolder.set(integer);
    }

    /**
     * 取得当前线程中的变量
     */
    public static Integer getCurrentThreadVariable() {
        return userHolder.get();
    }

    /**
     * 清除当前线程用户
     */
    public static void clearCurrentThreadVariable() {
        userHolder.remove();
    }

}
