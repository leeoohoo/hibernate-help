package com.learn.hibernate.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 不自动连表
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Nojoin {
    boolean value() default true;
}
