package com.xkw.mc.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkw.mc.dao.CourseDao;
import com.xkw.mc.entity.Course;
import com.xkw.mc.entity.Source;
import com.xkw.mc.service.CourseService;
import com.xkw.utils.Page;
@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, Integer> implements CourseService{

	@Autowired
	private CourseDao courseDao;
	@Override
	public Page<Course> getCourseList(Integer page, Integer rows, Map<String, Object> queryMap, Long sDate, Long eDate) {
		// TODO Auto-generated method stub
		return courseDao.getCourseList(page,rows, queryMap, sDate,eDate);
	}
	@Override
	public Page<Source> getSourceList(Integer page, Integer rows, Integer id) {
		// TODO Auto-generated method stub
		return courseDao.getSourceList(page,rows,id);
	}

	

}
