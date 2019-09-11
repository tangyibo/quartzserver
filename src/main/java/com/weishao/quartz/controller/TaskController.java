package com.weishao.quartz.controller;

import com.weishao.quartz.models.TaskEntity;
import com.weishao.quartz.service.TaskServiceMgr;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "TaskController", tags = {"定时任务操作"})
@RestController
@RequestMapping(value = "/job")
public class TaskController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskServiceMgr taskService;
    
    @GetMapping(value = "/jobClass")
	@ApiOperation(value="查询可用的任务执行类", notes="查询任务执行类")
    public Map<String,Object> jobClass() {
    	
		try {
			List<String> data = new ArrayList<String>();
			data.add(com.weishao.quartz.jobs.TestEchoJob.class.getName());
			data.add(com.weishao.quartz.jobs.HttpRequestJob.class.getName());
			return success(data);
		} catch (Exception e) {
			logger.error("error:", e);
			return failed(-1, e.getMessage());
		}
        
    }

    @PostMapping(value = "/add")
	@ApiOperation(value="添加任务", notes="添加一个定时任务")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "jobName", value = "job名称", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobGroup", value = "job分组", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobClass", value = "job执行类", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "cronExpression", value = "cron表达式", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobDescription", value = "job描述", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobParameters", value = "job参数", required = true, dataType = "String")
    })
    public Map<String,Object> addTask(String jobName, String jobGroup,String jobClass,String cronExpression,String jobDescription,String jobParameters) {

    	logger.info("add Task for Task name:"+jobName+",group:"+jobGroup);
    	try{
    		taskService.addTask(jobName,jobGroup,jobClass,cronExpression.trim(),jobDescription,jobParameters);
    		Map<String,Object> data=new HashMap<String,Object>();
    		data.put("jobName", jobName);
    		data.put("jobGroup", jobGroup);
    		return success(data);
    	}catch(Exception e) {
    		logger.error("error:",e);
    		return failed(-1,e.getMessage());
    	}
    }

    @PostMapping(value = "/update")
	@ApiOperation(value="更新任务", notes="通过jobName和jobGroup来更新一个定时任务")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "jobName", value = "job名称", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobGroup", value = "job分组", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "cronExpression", value = "cron表达式", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobDescription", value = "job描述", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobParameters", value = "job参数", required = true, dataType = "String")
    })
    public Map<String,Object> updateTask(String jobName, String jobGroup, String cronExpression,String jobDescription,String jobParameters) {
    	logger.info("update Task for Task name:"+jobName+",group:"+jobGroup);
        try {
        	taskService.updateTask(jobName,jobGroup,cronExpression,jobDescription,jobParameters);
        	return success(null);
        }catch(Exception e) {
    		logger.error("error:",e);
    		return failed(-1,e.getMessage());
    	}
    }

    @PostMapping(value = "/findJob")
	@ApiOperation(value="查询任务", notes="查询任务")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "jobName", value = "job名称", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobGroup", value = "job分组", required = true, dataType = "String")
    })
    public Map<String,Object> findJob(String jobName,String jobGroup) {
    	
		try {
			List<TaskEntity> dataList = taskService.findTaskList(jobName,jobGroup);
			logger.info("query all Task size:" + dataList.size());
			return success(dataList);
		} catch (Exception e) {
			logger.error("error:", e);
			return failed(-1, e.getMessage());
		}
        
    }
    
    @GetMapping(value = "/queryJob")
	@ApiOperation(value="查询所有任务", notes="查询所有任务")
    public Map<String,Object> queryJob() {
    	
		try {
			List<TaskEntity> dataList = taskService.queryTaskList();
			logger.info("query all Task size:" + dataList.size());
			return success(dataList);
		} catch (Exception e) {
			logger.error("error:", e);
			return failed(-1, e.getMessage());
		}
        
    }


    @PostMapping(value = "/delete")
	@ApiOperation(value="删除任务", notes="删除任务")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "jobName", value = "job名称", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobGroup", value = "job分组", required = true, dataType = "String")
    })
    public Map<String,Object> deleteTask(String jobName,String jobGroup ) {
    	logger.info("delete Task name:"+jobName+",group:"+jobGroup);
    	try {
			boolean flag = taskService.deleteTask(jobName, jobGroup);
			if (flag) {
				return success(null);
			}else {
				return failed(-1, "job not exist");
			}
    	} catch (Exception e) {
			logger.error("error:", e);
			return failed(-1, e.getMessage());
		}
    }


    @PostMapping(value = "/pause")
	@ApiOperation(value="暂停任务", notes="暂停任务")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "jobName", value = "job名称", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobGroup", value = "job分组", required = true, dataType = "String")
    })
    public Map<String,Object> pauseTask(String jobName,String jobGroup ) {
    	logger.info("pause Task name:"+jobName+",group:"+jobGroup);
        
    	try {
    		boolean flag = taskService.pauseTask(jobName, jobGroup);
			if (flag) {
				return success(null);
			}else {
				return failed(-1, "job not exist");
			}
    	} catch (Exception e) {
			logger.error("error:", e);
			return failed(-1, e.getMessage());
		}
    }


    @PostMapping(value = "/resume")
	@ApiOperation(value="恢复任务", notes="恢复任务")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "jobName", value = "job名称", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "jobGroup", value = "job分组", required = true, dataType = "String")
    })
    public Map<String,Object> resumeTask(String jobName,String jobGroup ) {
    	logger.info("resume Task name:"+jobName+",group:"+jobGroup);
        
    	try {
    		boolean flag = taskService.resumeTask(jobName, jobGroup);
			if (flag) {
				return success(null);
			}else {
				return failed(-1, "job not exist");
			}
    	} catch (Exception e) {
			logger.error("error:", e);
			return failed(-1, e.getMessage());
		}
    }

}