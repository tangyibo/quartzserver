package com.weishao.quartz.config.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(BrowerSecurityConfig.class);

	protected void configure(HttpSecurity http) throws Exception {
		logger.info("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");
		//http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
		http.csrf().disable().authorizeRequests().anyRequest().permitAll();
	}
}
