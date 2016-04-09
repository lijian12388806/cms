package com.xkw.mc.dao;


import java.util.Map;

import com.xkw.mc.entity.Course;
import com.xkw.mc.entity.Source;
import com.xkw.utils.Page;

public interface CourseDao extends BaseDao<Course, Integer>{

	Page<Course> getCourseList(Integer page, Integer rows, Map<String, Object> queryMap, Long sDate, Long eDate);

	Page<Source> getSourceList(Integer page, Integer rows, Integer id);


}
