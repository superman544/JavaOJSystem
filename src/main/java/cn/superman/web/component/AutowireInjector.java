package cn.superman.web.component;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AutowireInjector implements ApplicationContextAware {
    @Autowired
    private ApplicationContext applicationContext;

    public void autowire(Object bean) {
        this.applicationContext.getAutowireCapableBeanFactory().autowireBean(bean);
    }

    public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) {
        this.applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(existingBean, autowireMode, dependencyCheck);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
