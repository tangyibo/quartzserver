package com.weishao.quartz.service;

import com.weishao.quartz.exception.QuartzException;
import com.weishao.quartz.models.TaskEntity;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class TaskServiceMgr{
	
	private static final Logger logger = LoggerFactory.getLogger(TaskServiceMgr.class);
	
	@Autowired
	private Scheduler scheduler;

	/**
	 *  添加定时任务
	 */
	@SuppressWarnings("unchecked")
	public Boolean addTask(String jobName,String jobGroup,String jobClass,String cronExpression,String jobDescription,String jobParameters) {
		
		try {
			if (checkExists(jobName, jobGroup)) {
				throw new QuartzException(String.format("Job已经存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
			}
			
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
					.withMisfireHandlingInstructionDoNothing();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(jobDescription)
					.withSchedule(scheduleBuilder).build();

			Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobClass);
			JobDetail jobDetail = JobBuilder.newJob(clazz)
					.withIdentity(jobKey)
					.withDescription(jobDescription)
					.usingJobData("param", jobParameters)
					.build();
			scheduler.scheduleJob(jobDetail, trigger);
			
			return true;
		} catch (SchedulerException | ClassNotFoundException e) {
			logger.error("error:",e);
			throw new QuartzException("类名:"+jobClass+"不存在或执行表达式错误");
		}
	}

	/**
	 *  开始定时任务
	 */
	@Transactional(rollbackFor = Exception.class)
	public Boolean resumeTask(String jobName,String jobGroup) {
		try {
			scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
			return true;
		} catch (Exception e) {
			logger.error("error:",e);
			throw new QuartzException("FAILED:"+e.getMessage());
		}
	}

	/**
	 *  根据jobName和jobGroup查询job
	 */
	@Transactional(readOnly = true)
	public List<TaskEntity> findTaskList(String jobName,String jobGroup) {
		List<TaskEntity> list = new ArrayList<>();
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			if (scheduler.checkExists(triggerKey)) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(jobGroup))) {
					if(jobName.equals(jobKey.getName())) {
						List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
						for (Trigger trigger : triggers) {
							Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
							JobDetail jobDetail = scheduler.getJobDetail(jobKey);
	
							TaskEntity info = new TaskEntity();
							info.setJobName(jobKey.getName());
							info.setJobGroup(jobKey.getGroup());
							info.setJobClass(jobDetail.getJobClass().getName());
							info.setJobDescription(jobDetail.getDescription());
							info.setJobStatus(triggerState.name());
	
							CronTrigger cronTrigger = (CronTrigger) trigger;
							info.setCronExpression(cronTrigger.getCronExpression());
							info.setTimeZoneId(cronTrigger.getTimeZone().getID());
							
							list.add(info);
						}
					}
				}
			}

		} catch (SchedulerException e) {
			logger.error("error:",e);
		}

		return list;
	}

	/**
	 *  列举所有的job
	 */
	@Transactional(readOnly = true)
	public List<TaskEntity> queryTaskList() {

		List<TaskEntity> list = new ArrayList<>();
		
		try {

			for (String groupJob : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))) {
					List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
					for (Trigger trigger : triggers) {
						TriggerKey triggerKey=trigger.getKey();
						Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
						JobDetail jobDetail = scheduler.getJobDetail(jobKey);

						TaskEntity info = new TaskEntity();
						info.setJobName(jobKey.getName());
						info.setJobGroup(jobKey.getGroup());
						info.setJobClass(jobDetail.getJobClass().getName());
						info.setJobDescription(jobDetail.getDescription());
						info.setJobStatus(triggerState.name());
						info.setTriggerName(triggerKey.getName());
						info.setTriggerGroup(triggerKey.getGroup());
						info.setJobParameters(jobDetail.getJobDataMap().getString("param"));

						if (trigger instanceof CronTrigger) {
							CronTrigger cronTrigger = (CronTrigger) trigger;
							info.setCronExpression(cronTrigger.getCronExpression());
							info.setTimeZoneId(cronTrigger.getTimeZone().getID());
						} else if (trigger instanceof SimpleTrigger) {
							SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
							info.setStartTime(simpleTrigger.getStartTime().getTime());
							info.setEndTime(simpleTrigger.getEndTime().getTime());
						}else {
							throw new RuntimeException("Unkown trigger class type!");
						}
						
						list.add(info);
					}
				}
			}

		} catch (SchedulerException e) {
			logger.error("error:",e);
		}
		
		return list;
	}
	
	/**
	 * 修改定时任务
	 */
	public Boolean updateTask(String jobName,String jobGroup,String cronExpression,String jobDescription,String jobParameters) {
		
		String createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		
		try {
			if (!checkExists(jobName, jobGroup)) {
				throw new QuartzException(String.format("Job不存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
			}
			
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			JobKey jobKey = new JobKey(jobName, jobGroup);
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
					.withMisfireHandlingInstructionDoNothing();
			CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime)
					.withSchedule(cronScheduleBuilder).build();

			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			jobDetail.getJobBuilder().withDescription(jobDescription).usingJobData("param", jobParameters);
			HashSet<Trigger> triggerSet = new HashSet<>();
			triggerSet.add(cronTrigger);

			scheduler.scheduleJob(jobDetail, triggerSet, true);
			return true;
		} catch (SchedulerException e) {
			logger.error("error:",e);
			throw new QuartzException("执行表达式错误:"+e.getMessage());
		}
	}

	/**
	 *  停止任务
	 */
	@Transactional(rollbackFor = Exception.class)
	public Boolean pauseTask(String jobName,String jobGroup) {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		try {
			if (checkExists(jobName, jobGroup)) {
				scheduler.pauseTrigger(triggerKey); // 停止触发器
			}
			return true;
		} catch (Exception e) {
			logger.error("error:",e);
			throw new QuartzException(e.getMessage());
		}
	}

	/**
	 *  删除任务
	 */
	public Boolean deleteTask(String jobName,String jobGroup) {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName,jobGroup);
		try {
			if (checkExists(jobName,jobGroup)) {
				scheduler.pauseTrigger(triggerKey); // 停止触发器
				scheduler.unscheduleJob(triggerKey); // 移除触发器
				return true;
			}
		} catch (SchedulerException e) {
			logger.error("error:",e);
			throw new QuartzException(e.getMessage());
		}
		return false;
	}

	/**
	 * 验证是否存在
	 */
	private boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		return scheduler.checkExists(triggerKey);
	}
}
