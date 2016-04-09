package com.xkw.mc.dao.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xkw.mc.dao.CourseDao;
import com.xkw.mc.entity.Course;
import com.xkw.mc.entity.Source;
import com.xkw.utils.Page;
@Repository
public class CourseDaoImpl extends BaseDaoImpl<Course, Integer> implements CourseDao {

	//查询课件列表页
	@SuppressWarnings("unchecked")
	@Override
	public Page<Course> getCourseList(Integer page, Integer rows, Map<String, Object> queryMap, Long sDate, Long eDate) {
		StringBuffer sql = new StringBuffer("SELECT c, u.username "
				+ "FROM Course c, User u "
				+ "WHERE c.uid=u.id ");
		StringBuffer countSql = new StringBuffer("SELECT count(c.id) FROM Course c,User u WHERE c.uid=u.id ");
		if(sDate!=null) {
			sql.append("and c.createTime >= :sDate ");
			countSql.append("and c.createTime >= :sDate ");
		}
		if(eDate!=null) {
			sql.append("and c.createTime <= :eDate ");
			countSql.append("and c.createTime <= :eDate ");
		}
		if(queryMap!=null && queryMap.size()>0) {
			for(String key : queryMap.keySet()) {
				sql.append(" AND c.").append(key+"=:"+key);
				countSql.append(" AND c.").append(key+"=:"+key);
			}
		}
		
		Query query = getSession().createQuery(sql.toString());
		Query countQuery = getSession().createQuery(countSql.toString());
		query.setFirstResult((page-1)*rows).setMaxResults(rows);
		if(sDate!=null) {
			query.setParameter("sDate", sDate);
			countQuery.setParameter("sDate", sDate);
		}
		if(eDate!=null) {
			query.setParameter("eDate", eDate);
			countQuery.setParameter("eDate", eDate);
		}
		if(queryMap!=null && queryMap.size()>0) {
			for(String key : queryMap.keySet()) {
				query.setParameter(key, queryMap.get(key));
				countQuery.setParameter(key, queryMap.get(key));
			}
		}
		Long total = Long.parseLong(countQuery.uniqueResult().toString());
		List<Course> courses = new ArrayList<>();
		List<Object[]> content = query.list();
		for(Object[] objArr : content) {
			Course course = (Course) objArr[0];
			course.setUsername((String)objArr[1]);
			courses.add(course);
		}
		return new Page<>(courses, page, total, rows);
	}

	//所属课件的素材页
	@Override
	public Page<Source> getSourceList(Integer page, Integer rows, Integer id) {
		StringBuffer sql = new StringBuffer("SELECT s, u.username "
				+ "FROM Source s, User u "
				+ "WHERE s.uid=u.id "
				+ "AND s.courseId=:id "
				+ "AND s.type!=:type");
		StringBuffer countSql = new StringBuffer("SELECT count(s.id) FROM Source s,User u WHERE s.uid=u.id AND s.courseId=:id AND s.type!=:type");
		Query query = getSession().createQuery(sql.toString());
		Query countQuery = getSession().createQuery(countSql.toString());
		query.setParameter("type", Source.SourceTypes.GROUP);
		countQuery.setParameter("type", Source.SourceTypes.GROUP);
		if(id!=null) {
			query.setParameter("id", id);
			countQuery.setParameter("id", id);
		}
		query.setFirstResult((page-1)*rows).setMaxResults(rows);
		Long total = Long.parseLong(countQuery.uniqueResult().toString());
		List<Source> sources = new ArrayList<>();
		List<Object[]> content = query.list();
		for(Object[] objArr : content) {
			Source source = (Source) objArr[0];
			source.setUsername((String)objArr[1]);
			sources.add(source);
		}
		return new Page<>(sources, page, total, rows);
	}

	

}
