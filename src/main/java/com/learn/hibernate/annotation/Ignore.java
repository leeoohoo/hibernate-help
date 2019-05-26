package com.learn.hibernate.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 忽略实体类或Dto中的某个元素
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
    boolean value() default true;
}
