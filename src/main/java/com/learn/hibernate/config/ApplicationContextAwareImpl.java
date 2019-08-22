package com.learn.hibernate.config;

import com.learn.hibernate.utils.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 初始化spring 上下文
 * email leeoohoo@gmail.com
 * rocketmq
 * Created by lee on 2019-06-28.
 */
@Slf4j
@Component
@Data
public class ApplicationContextAwareImpl implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("应用程序上下文 ： [{}]", "开始初始化");
        SpringUtil.setApplicationContext(applicationContext);


        log.info("应用程序上下文 getId ： [{}]", applicationContext.getId());
        log.info("应用程序上下文 getApplicationName ： [{}]", applicationContext.getApplicationName());
        //  log.info("应用程序上下文 getAutowireCapableBeanFactory ： [{}]",applicationContext.getAutowireCapableBeanFactory());
        //  log.info("应用程序上下文 getDisplayName ： [{}]",applicationContext.getDisplayName());
        //  log.info("应用程序上下文 getParent ： [{}]",applicationContext.getParent());
        log.info("应用程序上下文 getStartupDate ： [{}]", applicationContext.getStartupDate());
        //  log.info("应用程序上下文 getEnvironment ： [{}]",applicationContext.getEnvironment());

        log.info("应用程序上下文 ： [{}]", "初始化完成");

    }
}
