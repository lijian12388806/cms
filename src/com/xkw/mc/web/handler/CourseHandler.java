package com.xkw.mc.web.handler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xkw.mc.entity.Course;
import com.xkw.mc.entity.Source;
import com.xkw.mc.service.CourseService;
import com.xkw.utils.Const;
import com.xkw.utils.DateUtils;
import com.xkw.utils.Page;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value="/course")
public class CourseHandler {

	@Autowired
	private CourseService courseService;
	
	/**
	 * 课件列表数据加开始、结束时间
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
			@RequestParam(required=false) String learnStages,
			@RequestParam(required=false) String subject,
			@RequestParam(required=false) String classes,
			@RequestParam(required=false) String version,
			@RequestParam(required=false) String startDate,
			@RequestParam(required=false) String endDate,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model,PrintWriter printWriter){
		page = (page !=null)||!"".equals(page)? page : Const.defPageNum;
		rows = (rows!=null)||!"".equals(rows) ? rows : Const.pageSize;
		Long sDate = null;
		try {
			sDate = DateUtils.valueOf(startDate).getTime();
		} catch (Exception e) {	}
		
		Long eDate = null;
		try {
			eDate = DateUtils.valueOf(endDate).getTime();
		} catch (Exception e) {	}
		
		Map<String, Object> queryMap = new HashMap<>();
		if(StringUtils.isNotEmpty(learnStages)){
			queryMap.put("learnStages",learnStages);
		}
		if(StringUtils.isNotEmpty(subject)){
			queryMap.put("subject",subject);
		}
		if(StringUtils.isNotEmpty(classes)){
			queryMap.put("classes",classes);
		}
		if(StringUtils.isNotEmpty(version)){
			queryMap.put("version",version);
		}
		
    	Page<Course> pageObj = courseService.getCourseList(page, rows, queryMap, sDate, eDate);
    	model.put("total", pageObj.getTotalElements());
		model.put("rows", pageObj.getContent());
		model.put("queryMap", queryMap);
		JSONObject jobj =JSONObject.fromObject(model);
		printWriter.println(jobj.toString());
	}
	
	/**
	 * 跳转到所属课件的素材页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toSource", method={RequestMethod.GET})
	public String tolist(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=true) Integer id,
			ModelMap model) {
		String name = courseService.getPropertyValueById(id, "name");
		
		model.put("id", id);
		model.put("name", name);
		return "course/courseSourceList";
	}
	
	/**
	 * 跳转到所属课件的素材页
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param model
	 * @param printWriter
	 */
	@RequestMapping(value="/courseSourceList", method={RequestMethod.POST})
	public void list(@RequestParam(required=false) Integer page,
			@RequestParam(required=false) Integer rows,
			@RequestParam(required=true) Integer id,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model,PrintWriter printWriter){
		page = (page !=null)||!"".equals(page)? page : Const.defPageNum;
		rows = (rows!=null)||!"".equals(rows) ? rows : Const.pageSize;
    	Page<Source> pageObj = courseService.getSourceList(page, rows, id);
    	model.put("total", pageObj.getTotalElements());
		model.put("rows", pageObj.getContent());
		JSONObject jobj =JSONObject.fromObject(model);
		printWriter.println(jobj.toString());
	}
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

