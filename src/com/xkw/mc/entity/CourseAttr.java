package com.xkw.mc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 课属性的实体封装类
 * @author anonymous
 *
 */
@Entity
@Table(name="t_course_attr")
public class CourseAttr extends SuperEntity {

	private static final long serialVersionUID = 3474627491574518509L;
	
	/**
	 * 属性的类别
	 * @author anonymous
	 *
	 */
	public enum AttrType {
		LEARNING_STAGES, //学段
		SUBJECT, //学科
		CLASS, //年级
		VERSION //版本
	}
	
	/**
	 * 课件属性的名称
	 */
	@Column(name="f_name", nullable=false, columnDefinition="varchar(30) COMMENT '课件属性的名称'")
	private String name;
	
	/**
	 * 类别
	 */
	@Column(name="f_type", nullable=false, columnDefinition="varchar(20) COMMENT '类别'")
	@Enumerated(EnumType.STRING)
	private AttrType type;
	
	/**
	 * 当前属性所关联的属性id
	 * examples: 
	 * 数学学科,关联的学段有小学,初中,高中
	 * 小年级,关联的是小学
	 * id之间以,隔开
	 * 例:1,5,65,21
	 */
	@Column(name="f_pids", columnDefinition="varchar(50) COMMENT '当前属性所关联的属性id, id之间以,隔开'")
	private String pids;

	public CourseAttr() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttrType getType() {
		return type;
	}

	public void setType(AttrType type) {
		this.type = type;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	@Override
	public String toString() {
		return "CourseAttr [name=" + name + ", type=" + type + ", pids=" + pids + ", id=" + id + "]";
	}
}
