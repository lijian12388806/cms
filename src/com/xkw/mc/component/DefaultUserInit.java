package com.xkw.mc.component;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.xkw.mc.entity.AdminUser;
import com.xkw.mc.service.AdminUserService;

/**
 * Spring容器加载完后, 检查数据库, 创建默认的管理员账号
 * @author anonymous
 *
 */
public class DefaultUserInit implements InitializingBean {

	/*是否需要创建默认用户，可以被设置为false使本Builder失效*/  
    private boolean open = true;  
    /*索引操作线程延时启动的时间，单位为秒*/  
    private int lazyTime = 5;
	
    private Thread indexThread = new Thread() {  
    	  
        @Override  
        public void run() {
        	try {
                Thread.sleep(lazyTime * 1000);
                AdminUserService userService = GlobalContext.getBean(AdminUserService.class);
                List<AdminUser> users = userService.getAll();
                if(users==null || users.size()<=0) {
                	AdminUser user = new AdminUser();
                	user.setEnabled(true);
                	user.setUsername("admin");
                	user.setPassword("admin01");
                	user.setRealName("默认管理员账号");
                	userService.saveUserBySecurity(user);
                }
            } catch (InterruptedException e) {
            }
        }  
    };
    
	@Override
	public void afterPropertiesSet() throws Exception {
		if (open) {  
            indexThread.setDaemon(true);  
            indexThread.setName("DefaultUserInit Thread");  
            indexThread.start();  
        }
	}
	
	public int getLazyTime() {
		return lazyTime;
	}
	
	public void setLazyTime(int lazyTime) {
		this.lazyTime = lazyTime;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
}
