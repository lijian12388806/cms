package com.xkw.mc.web.handler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xkw.mc.entity.CourseAttr;
import com.xkw.mc.service.CourseAttrService;
import com.xkw.utils.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/courseAttr")
public class CourseAttrHandler {

	@Autowired
	private CourseAttrService courseAttrService;
	/**
	 * 跳转至用户课件列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/tolist", method={RequestMethod.GET})
	public String tolist(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		//查询学段
		List<CourseAttr> learnStages = courseAttrService.getsByProperty("type", CourseAttr.AttrType.LEARNING_STAGES);
		//查询学科
		List<CourseAttr> subject = courseAttrService.getsByProperty("type", CourseAttr.AttrType.SUBJECT);
		//查询年级
		List<CourseAttr> classes = courseAttrService.getsByProperty("type", CourseAttr.AttrType.CLASS);
		//查询版本
		List<CourseAttr> version = courseAttrService.getsByProperty("type", CourseAttr.AttrType.VERSION);
		model.put("learnStages", learnStages);
		model.put("subject", subject);
		model.put("classes", classes);
		model.put("version", version);
		return "course/courseList";
	}
	/**
	 * 到学段列表页
	 * @return
	 */
	@RequestMapping(value="/tolearninglist", method={RequestMethod.GET})
	public String tolearninglist(HttpServletRequest request, HttpServletResponse response,
			ModelMap model){
		return "courseAttr/learningList";
	}
	/**
	 * 查询学段列表
	 * @return
	 */
	@RequestMapping(value="/learninglist", method={RequestMethod.POST})
	public void learninglist(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=false, defaultValue="1") Integer page,
			@RequestParam(required=false, defaultValue="20") Integer rows,
			ModelMap model,PrintWriter printWriter){
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("type", CourseAttr.AttrType.LEARNING_STAGES);
		Page<CourseAttr> pageObj = courseAttrService.getPage(page, rows, queryMap, null);
		model.put("total", pageObj.getTotalElements());
		model.put("rows", pageObj.getContent());
		JSONObject jobj =JSONObject.fromObject(model);
		printWriter.println(jobj.toString());
	}
	/**
	 * 创建或修改一个学段
	 * @param writer
	 * @param special
	 */
	@RequestMapping(value="/saveOrUpdateLearnStages", method ={RequestMethod.POST})
	public void saveOrUpdateLearnStages(PrintWriter writer,
    		@ModelAttribute("courseAttr") CourseAttr courseAttr){
		Map<String, Object> map = new HashMap<>();
		try {
			if(courseAttr.getId()==null) {
				courseAttr.setType(CourseAttr.AttrType.LEARNING_STAGES);
				courseAttrService.saveOrUpdate(courseAttr);
				map.put("success", true);
				map.put("msg", "创建成功");
			} else {
				CourseAttr dbCourseAttr = courseAttrService.getById(courseAttr.getId());
				if(StringUtils.isNotEmpty(courseAttr.getName())) {
					dbCourseAttr.setName(courseAttr.getName());
				}
				courseAttrService.saveOrUpdate(dbCourseAttr);
				map.put("success", true);
				map.put("msg", "修改成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "创建或修改失败");
		} finally {
			writer.println(JSONObject.fromObject(map));
		}
	}
	/**
     * 删除学段
     * @param id
     * @return
     */
    @RequestMapping(value="/delete", method=RequestMethod.POST) 
    public void delete(@RequestParam(value="id") Integer id,PrintWriter writer) {
    	Map<String, Object> map = new HashMap<>();
    	try {
    		courseAttrService.deleteById(id);
    		map.put("success", true);
    		map.put("msg", "删除成功");
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "删除失败");
		} finally {
			writer.println(JSONObject.fromObject(map));
		}
    }
	/**
	 * 到学科列表页
	 * @return
	 */
	@RequestMapping(value="/tosubjectlist", method={RequestMethod.GET})
	public String tosubjectlist(HttpServletRequest request, HttpServletResponse response,
			ModelMap model){
		List<CourseAttr> courseAttrs = courseAttrService.getsByProperty("type", CourseAttr.AttrType.LEARNING_STAGES);
		model.put("courseAttrs", courseAttrs);
		model.put("courseAttrJson", JSONArray.fromObject(courseAttrs).toString());
		return "courseAttr/subjectList";
	}
	/**
	 * 查询学科列表
	 * @return
	 */
	@RequestMapping(value="/subjectlist", method={RequestMethod.POST})
	public void subjectlist(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=false, defaultValue="1") Integer page,
			@RequestParam(required=false, defaultValue="20") Integer rows,
			ModelMap model,PrintWriter printWriter){
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("type", CourseAttr.AttrType.SUBJECT);
		Page<CourseAttr> pageObj = courseAttrService.getPage(page, rows, queryMap, null);
		//List<CourseAttr> courseAttrList = courseAttrService.getCourseAttrList();
		model.put("total", pageObj.getTotalElements());
		model.put("rows", pageObj.getContent());
		JSONObject jobj =JSONObject.fromObject(model);
		printWriter.println(jobj.toString());
	}
	/**
	 * 创建或修改一个学科
	 * @param writer
	 * @param special
	 */
	@RequestMapping(value="/saveOrUpdateSubject", method ={RequestMethod.POST})
	public void saveOrUpdateSubject(PrintWriter writer,
    		@ModelAttribute("courseAttr") CourseAttr courseAttr){
		Map<String, Object> map = new HashMap<>();
		try {
			if(courseAttr.getId()==null) {
				courseAttr.setType(CourseAttr.AttrType.SUBJECT);
				courseAttrService.saveOrUpdate(courseAttr);
				map.put("success", true);
				map.put("msg", "创建成功");
			} else {
				CourseAttr dbCourseAttr = courseAttrService.getById(courseAttr.getId());
				if(StringUtils.isNotEmpty(courseAttr.getName())) {
					dbCourseAttr.setName(courseAttr.getName());
				}
				if(StringUtils.isNotEmpty(courseAttr.getPids())) {
					dbCourseAttr.setPids(courseAttr.getPids());
				}
				courseAttrService.saveOrUpdate(dbCourseAttr);
				map.put("success", true);
				map.put("msg", "修改成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "创建或修改失败");
		} finally {
			writer.println(JSONObject.fromObject(map));
		}
	}
	/**
	 * 到年级列表页
	 * @return
	 */
	@RequestMapping(value="/toclasslist", method={RequestMethod.GET})
	public String toclasslist(HttpServletRequest request, HttpServletResponse response,
			ModelMap model){
		return "courseAttr/classList";
	}
	/**
	 * 到版本列表页
	 * @return
	 */
	@RequestMapping(value="/toversionlist", method={RequestMethod.GET})
	public String toversionlist(HttpServletRequest request, HttpServletResponse response,
			ModelMap model){
		return "courseAttr/versionList";
	}
	/**
	 * 查询版本列表
	 * @return
	 */
	@RequestMapping(value="/versionlist", method={RequestMethod.POST})
	public void versionlist(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=false, defaultValue="1") Integer page,
			@RequestParam(required=false, defaultValue="20") Integer rows,
			ModelMap model,PrintWriter printWriter){
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("type", CourseAttr.AttrType.VERSION);
		Page<CourseAttr> pageObj = courseAttrService.getPage(page, rows, queryMap, null);
		model.put("total", pageObj.getTotalElements());
		model.put("rows", pageObj.getContent());
		JSONObject jobj =JSONObject.fromObject(model);
		printWriter.println(jobj.toString());
	}
	/**
	 * 创建或修改一个版本
	 * @param writer
	 * @param special
	 */
	@RequestMapping(value="/saveOrUpdateVersion", method ={RequestMethod.POST})
	public void saveOrUpdateVersion(PrintWriter writer,
    		@ModelAttribute("courseAttr") CourseAttr courseAttr){
		Map<String, Object> map = new HashMap<>();
		try {
			if(courseAttr.getId()==null) {
				courseAttr.setType(CourseAttr.AttrType.VERSION);
				courseAttrService.saveOrUpdate(courseAttr);
				map.put("success", true);
				map.put("msg", "创建成功");
			} else {
				CourseAttr dbCourseAttr = courseAttrService.getById(courseAttr.getId());
				if(StringUtils.isNotEmpty(courseAttr.getName())) {
					dbCourseAttr.setName(courseAttr.getName());
				}
				courseAttrService.saveOrUpdate(dbCourseAttr);
				map.put("success", true);
				map.put("msg", "修改成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "创建或修改失败");
		} finally {
			writer.println(JSONObject.fromObject(map));
		}
	}
}
