package com.oovever.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Spring上下文工具类
 * @author OovEver
 * 2018/1/13 23:13
 */
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 获取srping的applicationContext
     * @param context spring的applicationContext
     * @throws BeansException 获取失败异常
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 获取bean实例
     * @param clazz 传入的class
     * @param <T> 传入的类型
     * @return 返回获取的bean
     */
    public static <T> T popBean(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    /**
     *获取bean实例
     * @param name spring bean的名称
     * @param clazz 传入的类
     * @param <T> 使用的类型
     * @return 返回获取的bean
     */
    public static <T> T popBean(String name, Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }
}
