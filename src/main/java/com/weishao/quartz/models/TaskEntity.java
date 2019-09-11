package com.weishao.quartz.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="TaskEntity",description="任务对象task")
public class TaskEntity {

    @ApiModelProperty(value="任务名称",name="jobName",required=true,dataType="String", example="test_job_01")
    private String jobName; 
    
    @ApiModelProperty(value="任务分组",name="jobGroup",required=true,dataType="String", example="test_group")
    private String jobGroup; 
    
    @ApiModelProperty(value="任务执行状态",name="jobStatus",required=false,dataType="String",hidden=true)
    private String jobStatus; 
    
    @ApiModelProperty(value="任务执行方法",name="jobClass",required=true, example="com.weishao.quartz.jobs.TesterJob")
    private String jobClass;
    
    @ApiModelProperty(value="cron 表达式",name="cronExpression",required=true, example="0/5 * * * * ? ")
    private String cronExpression;
    
    @ApiModelProperty(value="任务描述",name="jobDescription",required=false, example="this is a test cron job!")
    private String jobDescription;
    
    @ApiModelProperty(value="参数",name="jobParameters",required=false, example="exaple params!")
    private String jobParameters;
    
    @ApiModelProperty(value="触发器名称",name="triggerName",required=false,hidden=true)
    private String triggerName;
    
    @ApiModelProperty(value="触发器所在组",name="triggerGroup",required=false,hidden=true)
    private String triggerGroup;
    
    @ApiModelProperty(value="时区",name="timeZoneId",required=false,hidden=true)
    private String timeZoneId;
    
    @ApiModelProperty(value="开始时间",name="startTime",required=false,hidden=true)
    private Long startTime;    
    
    @ApiModelProperty(value="结束时间",name="endTime",required=false,hidden=true)
    private Long endTime;   
    
    @ApiModelProperty(value="状态",name="state",required=false,hidden=true)
    private String state;
    
    ////////////////////////////////////////////////////////////////////

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobParameters(String jobParameters) {
        this.jobParameters = jobParameters;
    }
    
    public String getJobParameters() {
        return jobParameters;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }


    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}