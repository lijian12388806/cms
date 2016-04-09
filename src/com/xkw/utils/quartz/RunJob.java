package com.xkw.utils.quartz;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.xkw.mc.component.GlobalContext;
import com.xkw.mc.entity.SchdulerJob;



/**
 * 用于任务执行的job
 * 最终的执行类
 * @author anonymous
 *
 */
public class RunJob implements Job{

	private static Logger logger = Logger.getLogger(RunJob.class);
	
	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		SchdulerJob schdulerJob = (SchdulerJob) ctx.getJobDetail().getJobDataMap().get("schdulerJob");
		String methodName = schdulerJob.getMethodName();
		String beanName = schdulerJob.getBeanName();
		try {
			Object bean = GlobalContext.getBean(beanName);

			/**
			 * 动态执行的方法, 统一成只有一个参数, 即为SchdulerJob
			 */
			Method method = bean.getClass().getMethod(methodName,SchdulerJob.class);
			method.invoke(bean, schdulerJob);
			logger.info("动态定时quartz任务执行成功");
		} catch (Exception e) {
			logger.error("任务job执行异常, schdulerJob: "+schdulerJob.toString(), e);
		}
	}
}
