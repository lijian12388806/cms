package com.xkw.mc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xkw.utils.Const;


/**
 * 一个定时任务的任务信息描述
 * @author anonymous
 *
 */
@Entity
@Table(name="t_schduler_job")
public class SchdulerJob implements Serializable{
	
	private static final long serialVersionUID = -8136533006242487188L;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	
    /** 
     * 使用执行方法+执行组对应的id
     */  
	@Column(name="f_jobName", length=100)
    private String jobName;  
   
    /** 
     * 任务分组 
     */  
	@Column(name="f_jobGroup", length=50)
    private String jobGroup;  
    
	/**
	 * 任务描述
	 */
	@Column(name="f_jobDes", length=50)
	private String jobDes;
	
	/**
	 * 运行参数
	 */
	@Column(name="f_runParam", length=200)
	private String runParam;
	
    /** 
     * cron表达式 
     */  
	@Column(name="f_cronExpression", length=100)
    private String cronExpression;  
   
    /** 
     * 任务执行时调用哪个类的方法 包名+类名 
     */  
	@Column(name="f_beanName", length=100)
    private String beanName;  
    
    /** 
     * 任务调用的方法名 
     */  
	@Column(name="f_methodName", length=100)
    private String methodName;

	/** 
     * 执行时间
     */  
	@Column(name="f_runTime")
	private long runTime;
	
	/**
	 * 任务状态
	 * 0  表示待执行
	 * 1 表示执行完毕
	 */
	@Column(name="f_status", columnDefinition="int(3)")
	private int status = 0;

	/**
	 * 应用名称
	 * 使用同一数据库时, 不同应用的定时任务是不一样的
	 */
	@Column(name="f_appName", length=20)
	private String appName = Const.QUARTZ_APPNAME;
	
	public SchdulerJob() {
		super();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public long getRunTime() {
		return runTime;
	}

	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getJobDes() {
		return jobDes;
	}

	public void setJobDes(String jobDes) {
		this.jobDes = jobDes;
	}

	public String getRunParam() {
		return runParam;
	}

	public void setRunParam(String runParam) {
		this.runParam = runParam;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public String toString() {
		return "SchdulerJob [id=" + id + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", jobDes=" + jobDes
				+ ", runParam=" + runParam + ", cronExpression=" + cronExpression + ", beanName=" + beanName
				+ ", methodName=" + methodName + ", runTime=" + runTime + ", status=" + status + ", appName=" + appName
				+ ", getId()=" + getId() + "]";
	}
}
