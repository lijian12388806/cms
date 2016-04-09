package com.xkw.mc.web.handler;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xkw.mc.entity.Source;
import com.xkw.mc.service.SourceService;
import com.xkw.utils.Const;
import com.xkw.utils.DateUtils;
import com.xkw.utils.Page;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/source")
public class SourceHandler {

	
	@Autowired
	private SourceService sourceService;
	/**
	 * 跳转至素材管理列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/tolist", method={RequestMethod.GET})
	public String tolist() {
		return "source/sourceList";
	}
	
	/**
	 * 素材列表数据加开始、结束时间
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
			@RequestParam(required=false) String startDate,
			@RequestParam(required=false) String endDate,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model,PrintWriter printWriter){
		page = (page !=null)||!"".equals(page)? page : Const.defPageNum;
		rows = (rows!=null)||!"".equals(rows) ? rows : Const.pageSize;
		Long sDate = null;
		Long eDate = null;
		
		try {
			sDate = DateUtils.valueOf(startDate).getTime();
		} catch (Exception e) { }
		try {
			eDate = DateUtils.valueOf(endDate).getTime();
		} catch (Exception e) { }
		
		Page<Source> pageObj = sourceService.getSourceList(page, rows, sDate, eDate);
    	model.put("total", pageObj.getTotalElements());
		model.put("rows", pageObj.getContent());
		JSONObject jobj =JSONObject.fromObject(model);
		printWriter.println(jobj.toString());
	}
	
	/**
     * 修改状态为通过
     * @return
     */
    @RequestMapping(value="/pass", method=RequestMethod.POST)
    public void pass(PrintWriter printWriter,
    		@RequestParam(required=true) Integer id,
    		@RequestParam(required=true) Integer status) {
    	JSONObject jsonObject = new JSONObject();
    	try {
    		sourceService.updatePropertyById(id, "status", status);
    		jsonObject.put("success", true);
    		jsonObject.put("msg", "修改成功");
    	} catch (Exception e) {
    		jsonObject.put("success", false);
    		jsonObject.put("msg", "修改失败");
    	}
    	printWriter.println(jsonObject.toString());
    }
    
    /**
	 * 批量审核通过
	 * @param Ids
	 * @param request
	 * @param response
	 * @param model
	 * @param printWriter
	 */
	@RequestMapping(value = "/multipleAudit",method=RequestMethod.POST)
	public void multipleAudit(
			@RequestParam(required=true) String ids,
			HttpServletRequest request, HttpServletResponse response,
			PrintWriter printWriter) {
		JSONObject jobj = new JSONObject();
		try {
			Integer status = 2;
			String[] idAry=ids.split(",");
			for(String id : idAry){
				Integer Id = Integer.parseInt(id);
				Source source = sourceService.getById(Id);
				source.setStatus(status);
				sourceService.update(source);
				jobj.put("success", true);
	    		jobj.put("msg", "修改成功");
			}
    	} catch (Exception e) {
    		jobj.put("success", false);
    		jobj.put("msg", "修改失败");
    	}
    	printWriter.println(jobj.toString());
	}
	/**
	 * 批量审核不通过
	 * @param Ids
	 * @param request
	 * @param response
	 * @param model
	 * @param printWriter
	 */
	@RequestMapping(value = "/multipleAuditNot",method=RequestMethod.POST)
	public void multipleAuditNot(
			@RequestParam(required=true) String ids,
			HttpServletRequest request, HttpServletResponse response,
			PrintWriter printWriter) {
		JSONObject jobj = new JSONObject();
		try {
			Integer status = 1;
			String[] idAry=ids.split(",");
			for(String id : idAry){
				Integer Id = Integer.parseInt(id);
				Source source = sourceService.getById(Id);
				source.setStatus(status);
				sourceService.update(source);
				jobj.put("success", true);
	    		jobj.put("msg", "修改成功");
			}
    	} catch (Exception e) {
    		jobj.put("success", false);
    		jobj.put("msg", "修改失败");
    	}
    	printWriter.println(jobj.toString());
	}
	
	/**
	 * 删除
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/delete", method={RequestMethod.POST})
	public void delete(@RequestParam(value="id") Integer id,PrintWriter writer) {
		Map<String, Object> map = new HashMap<>();
    	try {
    		sourceService.deleteSource(id);
    		map.put("success", true);
    		map.put("msg", "删除成功");
		} catch (IOException | MyException e) {
			map.put("success", false);
			map.put("msg", "FastDFS文件删除失败");
		} catch(Exception e) {
			map.put("success", false);
			map.put("msg", "删除失败");
		} finally {
			writer.println(JSONObject.fromObject(map));
		}
	}
}
