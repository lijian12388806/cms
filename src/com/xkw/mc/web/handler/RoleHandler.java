package com.xkw.mc.web.handler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xkw.mc.entity.Authority;
import com.xkw.mc.entity.Role;
import com.xkw.mc.service.AuthorityService;
import com.xkw.mc.service.RoleService;
import com.xkw.utils.Const;
import com.xkw.utils.Page;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;


@Controller
@RequestMapping(value="/role")
public class RoleHandler {

	private static Logger logger = Logger.getLogger(RoleHandler.class);
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthorityService authorityService;
	
	/**
	 * 角色列表数据
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param model
	 * @param printWriter
	 */
	@RequestMapping(value="/list", method={RequestMethod.POST})
	public void list(@RequestParam(required=false) Integer page,
			@RequestParam(required=false) Integer rows,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model,PrintWriter printWriter){
		page = (page !=null)||!"".equals(page)? page : Const.defPageNum;
		rows = (rows!=null)||!"".equals(rows) ? rows : Const.pageSize;
    	Page<Role> pageObj = roleService.getPage(page, rows, null, null);
		model.put("total", pageObj.getTotalElements());
		model.put("rows"	, pageObj.getContent());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"authorities"});
		JSONObject jobj =JSONObject.fromObject(model, jsonConfig );
		printWriter.println(jobj.toString());
	}
	
	/**
     * 添加/修改角色信息
     * @return
     */
    @RequestMapping(value="/saveOrUpdate", method=RequestMethod.POST)
    public void saveOrUpdate(HttpServletRequest request, PrintWriter printWriter,
    		@ModelAttribute("role") Role role) {
    	Map<String, Object> map = new HashMap<>();
    	try {
    		if(role!=null) {
    			if(role.getId()==null) { //是新用户
    				roleService.saveOrUpdate(role);;
    				map.put("msg", "角色添加成功");
    			} else {
    				Role dbRole = roleService.getById(role.getId());
    				if(StringUtils.isNotEmpty(role.getDisplayName())) {
    					dbRole.setDisplayName(role.getDisplayName());
    				} else {
    					map.put("msg", "角色名称不能空");
    					map.put("success", false);
    					return ;
    				}
					dbRole.setEnabled(role.getEnabled());
    				roleService.update(dbRole);
    				map.put("msg", "角色修改成功");
    			}
    			map.put("success", true);
    			return ;
    		}
    		map.put("success", false);
    	} catch (Exception e) {
    		map.put("msg", "角色添加或修改失败, 服务器异常, 请稍后再试");
    		map.put("success", false);
    		logger.error("角色添加或修改失败", e);
    	} finally {
    		printWriter.print(JSONObject.fromObject(map).toString());
    	}
    }
    
    /**
     * 跳转至角色的权限分配页面
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value="/toAssign", method={RequestMethod.GET})
    public String toAssign(ModelMap model, @RequestParam(required=true) Integer id) {
    	Role role = roleService.getById(id);
    	if(role!=null) {
    		List<String> userAuths = role.getUserAuths();
    		model.put("userAuths", userAuths.toString().replace("[", ",").replace("]", ",").replace(" ", ""));
    	}
    	
    	List<Authority> auths = authorityService.getRootAuthorities();
    	
    	model.put("role", role);
    	model.put("auths", auths);
    	return "role/assign";
    }
    
    /**
     * 
     * @param destUser
     * @param writer
     * @param userId
     * @param userAuthIds
     */
    @RequestMapping(value="/updateAuthority", method=RequestMethod.POST)
    public void updateAuthority(PrintWriter writer,
    		@RequestParam(required=true) Integer id, 
    		@RequestParam List<String> userAuthIds) {
    	Map<String, Object> map = new HashMap<>();
    	try {
    		Role role = roleService.getById(id);
    		role.setUserAuths(userAuthIds);
    		roleService.saveOrUpdate(role);
	    	map.put("success", true);
	    	map.put("msg", "修改成功");
    	} catch (Exception e) {
    		e.printStackTrace();
    		map.put("success", "修改失败");
    	} finally {
    		writer.println(JSONObject.fromObject(map));
    	}
    }
}
