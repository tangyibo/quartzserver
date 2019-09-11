package com.weishao.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEchoJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(TestEchoJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String param = dataMap.getString("param");
		log.info("test echo param : " + param);
	}
}