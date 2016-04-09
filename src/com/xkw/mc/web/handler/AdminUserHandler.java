package com.xkw.mc.web.handler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xkw.mc.component.security.McUserDetailsService.SecurityUser;
import com.xkw.mc.entity.AdminUser;
import com.xkw.mc.entity.Role;
import com.xkw.mc.service.AdminUserService;
import com.xkw.mc.service.RoleService;
import com.xkw.utils.Const;
import com.xkw.utils.Page;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 管理员用户请求处理器
 * @author anonymous
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminUserHandler {

	private static Logger logger = Logger.getLogger(AdminUserHandler.class);
	
	@Autowired
	private AdminUserService userService;
	
	@Autowired
	private RoleService roleService;
	
	
	/**
	 * 跳转至管理列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/tolist", method={RequestMethod.GET})
	public String tolist(ModelMap model) {
		List<Role> roles = roleService.getsByProperty("enabled", true);
		model.put("roles", roles);
		
		return "user/userList";
	}
	
	
	/**
	 * 登录操作 
	 * @param session
	 * @param model
	 * @param rememberme
	 * @return
	 */
    @RequestMapping(value = "/login", method = {RequestMethod.GET})  
    public String getLoginPage(HttpSession session,ModelMap model,
            @RequestParam(value = "rememberme", required  = false) String rememberme) {  
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof SecurityUser) {
			SecurityUser securityUser = (SecurityUser) principal;
			session.setAttribute("user", securityUser.getUser());
			session.setMaxInactiveInterval(15*60);
		}
        
        return "redirect:/admin/welcome";
    }
    
    
    /**
     * 用户列表
     * @param map
     * @param page
     * @param rows
     * @param request
     * @param response
     * @param model
     * @param printWriter
     */
    @RequestMapping(value="/list", method ={RequestMethod.GET,RequestMethod.POST})
    public void list(Map<String, Object> map,
			@RequestParam(required=false) Integer page,
			@RequestParam(required=false) Integer rows,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model,PrintWriter printWriter){
		page = (page !=null)||!"".equals(page)? page : Const.defPageNum;
		rows = (rows!=null)||!"".equals(rows) ? rows : Const.pageSize;
    	Page<AdminUser> pageObj = userService.getPage(page, rows, null, null);
		model.put("total", pageObj.getTotalElements());
		model.put("rows", pageObj.getContent());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"authorities"});
		JSONObject jobj =JSONObject.fromObject(model, jsonConfig );
		printWriter.println(jobj.toString());
    }
    
    
    /**
     * 删除一个用户
     * @param id
     * @return
     */
    @RequestMapping(value="/delete", method=RequestMethod.GET) 
    public String delete(@RequestParam(value="id") Integer id,
    		HttpServletRequest request, HttpServletResponse response) {
    	userService.deleteById(id);;
    	return "redirect:/admin/list";
    }
    
    
    /**
     * 跳转至个人设置页面
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value="/account", method=RequestMethod.GET)
    public String myAccount(HttpSession session, Map<String, Object> map) {
    	AdminUser user = (AdminUser) session.getAttribute("user");
    	map.put("user", user);
    	return "user/account";
    }
    
    
    /**
     * 账户管理,用户自身账户信息的修改
     * 只限修改密码
     * @return
     */
    @RequestMapping(value="/updateMyAccount", method=RequestMethod.POST)
    public void updateMyAccount(AdminUser user, Map<String, Object> map,HttpServletRequest request
    		,PrintWriter printWriter) {
    	if(user!=null) {
    		AdminUser adminUser = userService.getById(user.getId());
    		adminUser.setPassword(user.getPassword());
    		userService.saveUserBySecurity(adminUser);
    		JSONObject json=new JSONObject();
    		json.put("msg", "密码修改成功");
    		json.put("success", true);
    		printWriter.print(json);
    		return ;
    	}
    	JSONObject json=new JSONObject();
		json.put("msg", "密码修改失败");
		json.put("success", false);
		printWriter.print(json);
    }
    
    
    /**
     * 修改密码
     * @return
     */
    @RequestMapping(value="/updatePwd", method=RequestMethod.POST)
    public void updatePwd(Map<String, Object> map,HttpServletRequest request, PrintWriter printWriter,
    		@RequestParam(required=true) Integer id, 
    		@RequestParam(required=true) String password) {
    	AdminUser user = userService.getById(id);
    	try {
	    	if(user!=null) {
	    		user.setPassword(password);
	    		userService.saveUserBySecurity(user);
	    		map.put("msg", "密码修改成功");
	    		map.put("success", true);
	    		return;
	    	}
	    	map.put("msg", "密码修改失败");
	    	map.put("success", false);
    	} catch (Exception e) {
    		map.put("msg", "密码修改失败");
    		map.put("success", false);
    		e.printStackTrace();
    	} finally {
    		printWriter.print(JSONObject.fromObject(map).toString());
    	}
    }
    
    
    /**
     * 修改管理员账号信息
     * @return
     */
    @RequestMapping(value="/saveOrUpdate", method=RequestMethod.POST)
    public void saveOrUpdate(HttpServletRequest request, PrintWriter printWriter,
    		@ModelAttribute("adminUser") AdminUser adminUser) {
    	Map<String, Object> map = new HashMap<>();
    	try {
    		if(adminUser!=null) {
    			if(adminUser.getId()==null) { //是新用户
    				userService.saveUserBySecurity(adminUser);
    			} else {
    				AdminUser dbUser = userService.getById(adminUser.getId());
    				if(StringUtils.isNotEmpty(adminUser.getCellphone())) {
    					dbUser.setCellphone(adminUser.getCellphone());
    				} else {
    					dbUser.setCellphone(null);
    				}
    				if(StringUtils.isNotEmpty(adminUser.getEmail())) {
    					dbUser.setEmail(adminUser.getEmail());
    				} else {
    					dbUser.setEmail(null);
    				}
    				if(StringUtils.isNotEmpty(adminUser.getRealName())) {
    					dbUser.setRealName(adminUser.getRealName());
    				} else {
    					dbUser.setRealName(null);
    				}
    				if(adminUser.getRole()!=null) {
    					dbUser.setRole(adminUser.getRole());
    				}
    				dbUser.setEnabled(adminUser.getEnabled());
    				userService.update(dbUser);
    			}
    			map.put("msg", "用户添加或修改成功");
    			map.put("success", true);
    			return ;
    		}
    		map.put("msg", "用户添加或修改失败");
    		map.put("success", false);
    	} catch (Exception e) {
    		map.put("msg", "用户添加或修改失败");
    		map.put("success", false);
    		logger.error("用户添加或修改失败", e);
    	} finally {
    		printWriter.print(JSONObject.fromObject(map).toString());
    	}
    }
}
