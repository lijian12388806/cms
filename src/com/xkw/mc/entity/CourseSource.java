package com.xkw.mc.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 课件资源内容的封装类
 * @author anonymous
 *
 */
@Entity
@Table(name="t_course_source")
public class CourseSource extends SuperEntity {

	private static final long serialVersionUID = -4017422507539147438L;
	
	/**
	 * 所对应的资源
	 */
	@OneToOne
	@JoinColumn(name="f_sourceId", columnDefinition="int(11) COMMENT '所对应的资源'")
	private Source source;
	
	/**
	 * 当前内容的父内容,用于处理课件内容中一张图片包含多个音频的情况
	 * 此属性即是音频所属于哪一个资源
	 */
	@ManyToOne
	@JoinColumn(name="f_parentId", columnDefinition="int(11) COMMENT '当前内容的父内容,用于处理课件内容中一张图片包含多个音频的情况'")
	private CourseSource parent;
	
	/**
	 * 当前内容的父内容,用于处理课件内容中一张图片包含多个音频的情况
	 *此属性即图片所包含的音频
	 */
	@OneToMany(mappedBy="parent")
	private Set<CourseSource> subs;
	
	/**
	 * 排序
	 */
	@Column(name="f_sort", columnDefinition="int(6) DEFAULT 0 COMMENT '排序'")
	private Integer sort;

	/**
	 * 该课件内容所属的课件
	 */
	@Column(name="f_courseId", columnDefinition="int(11) COMMENT '该课件内容所属的课件'")
	private Integer courseId;


	/**
	 * 音频文件在图片或视频上的横坐标
	 */
	@Column(name="f_coordX", columnDefinition="float DEFAULT 0 COMMENT '音频文件在图片或视频上的横坐标'")
	private Float coordX;
	
	/**
	 * 音频文件在图片或视频上的纵坐标
	 */
	@Column(name="f_coordY", columnDefinition="float DEFAULT 0 COMMENT '音频文件在图片或视频上的纵坐标'")
	private Float coordY;
	
	
	public CourseSource() {
		super();
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public CourseSource getParent() {
		return parent;
	}

	public void setParent(CourseSource parent) {
		this.parent = parent;
	}

	public Set<CourseSource> getSubs() {
		return subs;
	}

	public void setSubs(Set<CourseSource> subs) {
		this.subs = subs;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public Float getCoordX() {
		return coordX;
	}

	public void setCoordX(Float coordX) {
		this.coordX = coordX;
	}

	public Float getCoordY() {
		return coordY;
	}

	public void setCoordY(Float coordY) {
		this.coordY = coordY;
	}

	@Override
	public String toString() {
		return "CourseSource [source=" + source + ", sort=" + sort + ", courseId=" + courseId + ", coordX=" + coordX
				+ ", coordY=" + coordY + ", id=" + id + "]";
	}

}
