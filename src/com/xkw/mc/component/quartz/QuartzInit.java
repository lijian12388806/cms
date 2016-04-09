package com.xkw.mc.component.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.InitializingBean;

import com.xkw.mc.component.GlobalContext;
import com.xkw.mc.entity.SchdulerJob;
import com.xkw.mc.service.SchdulerJobService;
import com.xkw.utils.Const;
import com.xkw.utils.SearchOperator;
import com.xkw.utils.quartz.QuartzManager;


/**
 * 动态quartz的初始化
 * 即项目重启时, 从数据库中获取任务, 添加到quartz管理器的
 * @author anonymous
 *
 */
public class QuartzInit implements InitializingBean {

	private static Logger logger = Logger.getLogger(QuartzInit.class);
	
	//是否进行动态的quartz初始化
    private boolean init = true;  
    
    //延迟时间
    private int lazyTime = 5;
    
    private SchdulerJobService jobService;
	
	private Thread initQuartzThread = new Thread() {
		public void run() {
			try {
				Thread.sleep(lazyTime * 1000);
				logger.warn("动态quartz的初始化, 获取数据库中的任务并添加到quartz管理器中");
				
				List<SearchOperator> searchs = new ArrayList<>();
				searchs.add(new SearchOperator("status", SearchOperator.Operator.EQ, 0));
				searchs.add(new SearchOperator("appName", SearchOperator.Operator.EQ, Const.QUARTZ_APPNAME));
				
				List<SchdulerJob> jobs = jobService.generalQuery(searchs , null);
				Object bean = GlobalContext.getBean("scheduler");
				if(bean!=null && bean instanceof StdScheduler) {
					StdScheduler scheduler = (StdScheduler) bean;
					for(SchdulerJob job : jobs) {
						if(job.getRunTime()>System.currentTimeMillis()) {
							QuartzManager.addJob(scheduler, job);
						} else {
							jobService.delete(job);
						}
					}
				}
			} catch (Exception e) {
				logger.error("动态的quartz任务添加失败", e);
			}
		};
	};
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(init) {
			initQuartzThread.setDaemon(true);  
			initQuartzThread.setName("QuartzInit Thread");  
			initQuartzThread.start();
		}
	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public int getLazyTime() {
		return lazyTime;
	}

	public void setLazyTime(int lazyTime) {
		this.lazyTime = lazyTime;
	}

	public SchdulerJobService getJobService() {
		return jobService;
	}

	public void setJobService(SchdulerJobService jobService) {
		this.jobService = jobService;
	}
}
