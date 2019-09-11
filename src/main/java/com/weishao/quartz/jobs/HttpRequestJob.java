package com.weishao.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weishao.quartz.config.app.SpringContextManager;

public class HttpRequestJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(HttpRequestJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		
		/**HTTP任务的参数格式
		 * {
		 *    "url":"http://www.baidu.com",
		 *    "method":"get",
		 *    "data":null
		 * }
		 */
		String params=null;
		String response=null;
		long startTime = System.currentTimeMillis();
		try {
			params = dataMap.getString("param");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(params);
			String url = rootNode.path("url").asText();
			String method = rootNode.path("method").asText();
			String data = rootNode.path("data").asText();

			ApplicationContext appctx=SpringContextManager.getApplicationContext();
			RestTemplate restTemplate=(RestTemplate) appctx.getBean("restTemplate");
			
			if (method.toLowerCase().equals("get")) {
				response = restTemplate.getForObject(url, String.class);
			} else if (method.toLowerCase().equals("post")) {
				response = restTemplate.postForObject(url, data, String.class);
			} else {
				log.warn("Unkown http method: " + params);
			}
		} catch (Exception e) {
			log.error("error message:", e);
		} finally {
			long endTime = System.currentTimeMillis();
			float seconds = (endTime - startTime) / 1000F;
			log.info(String.format("handle for task elipse %s seconds. data is: %s,\n response is:%s",Float.toString(seconds),params,response));
		}
	}

}
