package com.weishao.quartz.config.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextManager implements ApplicationContextAware {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringContextManager.class);

	private static ApplicationContext context = null;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextManager.context = applicationContext;
		logger.info("###### Init ApplicationContext success! ");
	}

	/**
	 * 获取applicationContext
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return SpringContextManager.context;
	}

	/**
	 * 通过name获取 Bean.
	 * 
	 * @param name
	 */
	public static Object getBean(String name) {
		return SpringContextManager.getApplicationContext().getBean(name);
	}

	/**
	 * 通过class获取Bean.
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return SpringContextManager.getApplicationContext().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 * 
	 * @param name
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return SpringContextManager.getApplicationContext().getBean(name, clazz);
	}

}
