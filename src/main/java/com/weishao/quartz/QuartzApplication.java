package com.weishao.quartz;

import com.weishao.quartz.config.app.AppConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }

    @Bean
    public AppConfig appConfig() {
        return new AppConfig();
    }
    
    @Bean(value="restTemplate")
	public RestTemplate restTemplate() {
    	SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	    requestFactory.setConnectTimeout(5*1000);//5s
	    requestFactory.setReadTimeout(60*1000);//60s
		return new RestTemplate(requestFactory);
	}
}
