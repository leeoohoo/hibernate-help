package com.learn.hibernate;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.base.LQuery;
import com.learn.hibernate.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
@EntityScan({"com.learn.hibernate.entity","com.learn.hibernate.common.entity"})
public class HibernateApplication {

    public static void main(String[] args) {
        SpringApplication.run(HibernateApplication.class, args);
    }



}
