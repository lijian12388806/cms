package com.xkw.mc.web.handler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xkw.mc.entity.Authority;
import com.xkw.mc.service.AuthorityService;
import com.xkw.utils.Const;
import com.xkw.utils.Page;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping(value="/authority")
public class AuthorityHandler {

	private static Logger logger = Logger.getLogger(AuthorityHandler.class);
	
	@Autowired
	private AuthorityService authorityService;
	
	/**
	 *跳转至权限列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/tolist", method={RequestMethod.GET})
	public String tolist(ModelMap model) {
		List<Authority> rootAuths = authorityService.getRootAuthorities();
		
		model.put("rootAuths", rootAuths);
		return "authority/authorityList";
	}
	
	
	/**
	 * 权限列表数据
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
    	Page<Authority> pageObj = authorityService.getPage(page, rows, null, null);
		model.put("total", pageObj.getTotalElements());
		model.put("rows"	, pageObj.getContent());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"subAuthorities", "authority"});
		JSONObject jobj =JSONObject.fromObject(model, jsonConfig );
		printWriter.println(jobj.toString());
	}
	
	
	/**
	 * 添加或修改权限
	 * @param request
	 * @param printWriter
	 * @param authority
	 * @param urls
	 */
	@RequestMapping(value="/saveOrUpdate", method=RequestMethod.POST)
    public void saveOrUpdate(HttpServletRequest request, PrintWriter printWriter,
    		@ModelAttribute Authority authority,
    		@RequestParam List<String> urls) {
    	Map<String, Object> map = new HashMap<>();
    	try {
    		if(authority!=null) {
    			if(authority.getId()==null) { //是新用户
    				authorityService.saveOrUpdateResource(authority, urls);
    				map.put("msg", "权限添加成功");
    			} else {
    				authorityService.saveOrUpdateResource(authority, urls);
    				map.put("msg", "权限修改成功");
    			}
    			map.put("success", true);
    			return ;
    		}
    		map.put("success", false);
    	} catch (Exception e) {
    		map.put("msg", "权限添加或修改失败, 服务器异常, 请稍后再试");
    		map.put("success", false);
    		logger.error("权限添加或修改失败", e);
    	} finally {
    		printWriter.print(JSONObject.fromObject(map).toString());
    	}
    }
	
}
