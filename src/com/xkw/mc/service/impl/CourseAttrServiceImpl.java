package com.xkw.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkw.mc.dao.CourseAttrDao;
import com.xkw.mc.entity.CourseAttr;
import com.xkw.mc.service.CourseAttrService;
@Service
public class CourseAttrServiceImpl extends BaseServiceImpl<CourseAttr, Integer> implements CourseAttrService{

	@Autowired
	private CourseAttrDao courseAttrDao;
}
