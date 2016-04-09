package com.xkw.utils.quartz;

import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;

import com.xkw.mc.entity.SchdulerJob;
import com.xkw.utils.DateUtils;



/**
 * 动态添加或删除定时任务
 * @author anonymous
 *
 */
public class QuartzManager {
	
	private static Logger logger = Logger.getLogger(QuartzManager.class);
	
	/**
	 * 添加或更新一个定时任务
	 * @param scheduler quartz的调度器
	 * @param job
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static void addJob(StdScheduler scheduler, SchdulerJob job) throws SchedulerException, ParseException {
    	TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
    	Trigger trigger = scheduler.getTrigger(triggerKey);
    	
    	if(job.getRunTime() <= System.currentTimeMillis()) {
    		remove(scheduler, job);
    		logger.warn("运行时间不合法, 小于或等于当前时间");
    		return ;
    	}
    	
    	if(trigger==null) {//如果是新任务, 则创建
            JobDetail jobDetail = JobBuilder.newJob(RunJob.class).withIdentity(job.getJobName(), job.getJobGroup()).build(); 
            jobDetail.getJobDataMap().put("schdulerJob", job);
    		trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup())
    				.withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression(job.getCronExpression()))).build();
    		scheduler.scheduleJob(jobDetail, trigger);
    	} else {//如果是已存在任务
    		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(new CronExpression(job.getCronExpression()));  
            // 按新的cronExpression表达式重新构建trigger  
            trigger = ((TriggerBuilder<Trigger>)trigger.getTriggerBuilder().withIdentity(triggerKey))
            		.withSchedule(scheduleBuilder).build();
  
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);  
    	}
	}
	
	/**
	 * 删除一个定时任务
	 * @param scheduler quartz的调度器
	 * @param job
	 * @throws SchedulerException
	 */
	public static void remove(StdScheduler scheduler, SchdulerJob job) throws SchedulerException {
		JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
		scheduler.deleteJob(jobKey);
	}
	
	/**
	 * 获取指定执行时间的cron表达式
	 * @param date
	 * @param pDays
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getCronExpression(Date date, int pDays) {
		Date runDate = DateUtils.getDaysIntervalDate(date, pDays);
		/**
		 * cron格式: 
		 * 41 48 16 9 12 ? 2015
		 * 秒   分   时   天   月   月中的星期  年
		 */
		StringBuffer expression = new StringBuffer();
		expression.append(runDate.getSeconds()+" ");
		expression.append(runDate.getMinutes()+" ");
		expression.append(runDate.getHours()+" ");
		expression.append(runDate.getDate()+" ");
		expression.append((runDate.getMonth()+1)+" ");
		expression.append("? ");
		expression.append(DateUtils.getYear(runDate));
		
		return expression.toString();
	}
	
	/**
	 * 获取指定执行时间的cron表达式
	 * @param time
	 * @param pDays
	 * @return
	 */
	public static String getCronExpression(long time, int pDays) {
		return getCronExpression(new Date(time), pDays);
	}
	
	/**
	 * 获取指定执行时间的cron表达式
	 * 多少天以后执行
	 * @param date
	 * @param pDays
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getCronExpression(Date runDate) {
		/**
		 * cron格式: 
		 * 41 48 16 9 12 ? 2015
		 * 秒   分   时   天   月   月中的星期  年
		 */
		StringBuffer expression = new StringBuffer();
		expression.append(runDate.getSeconds()+" ");
		expression.append(runDate.getMinutes()+" ");
		expression.append(runDate.getHours()+" ");
		expression.append(runDate.getDate()+" ");
		expression.append((runDate.getMonth()+1)+" ");
		expression.append("? ");
		expression.append(DateUtils.getYear(runDate));
		
		return expression.toString();
	}

	/**
	 * 获取指定执行时间的cron表达式
	 * @param time
	 * @param pDays
	 * @return
	 */
	public static String getCronExpression(long time) {
		return getCronExpression(new Date(time));
	}
}
