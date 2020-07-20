package io.species.analyzer.infrastructure.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class DefaultApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public synchronized void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getBean(final String beanName) {
        return getBean(beanName, Object.class);
    }

    public static <T> T getBean(final String beanName, final Class<T> clazz) {
        return ObjectUtils.isEmpty(beanName) ? applicationContext.getBean(clazz) : applicationContext.getBean(beanName, clazz);
    }
}

