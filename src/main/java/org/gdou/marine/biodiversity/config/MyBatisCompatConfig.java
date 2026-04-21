package org.gdou.marine.biodiversity.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class MyBatisCompatConfig implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) beanFactory.getBeanDefinition(beanName);
            if (beanDefinition.hasAttribute("factoryBeanObjectType")) {
                Object value = beanDefinition.getAttribute("factoryBeanObjectType");
                if (value instanceof String className) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        beanDefinition.setAttribute("factoryBeanObjectType", clazz);
                    } catch (ClassNotFoundException e) {
                        // ignore
                    }
                }
            }
        }
    }
}
