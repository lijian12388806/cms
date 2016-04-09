package com.xkw.mc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 教师课件的封装实体
 * @author anonymous
 */
@Entity
@Table(name="t_course")
public class Course extends SuperEntity {

	private static final long serialVersionUID = 7559578455024910565L;
	
	/**
	 * 课件名称
	 */
	@Column(name="f_name", nullable=false, columnDefinition="varchar(30) COMMENT '课件名称'")
	private String name;
	
	/**
	 * 学段
	 */
	@Column(name="f_learnStages", nullable=false, columnDefinition="varchar(30) COMMENT '学段'")
	private String learnStages;
	
	/**
	 * 学科
	 */
	@Column(name="f_subject", nullable=false, columnDefinition="varchar(30) COMMENT '学科'")
	private String subject;
	
	/**
	 * 年级
	 */
	@Column(name="f_classes", nullable=false, columnDefinition="varchar(30) COMMENT '年级'")
	private String classes;
	
	/**
	 * 版本
	 */
	@Column(name="f_version", nullable=false, columnDefinition="varchar(30) COMMENT '版本'")
	private String version;

	/**
	 * 课件创建时间
	 */
	@Column(name="f_createTime", nullable=false, columnDefinition="bigint(20) COMMENT '课件创建时间'")
	private Long createTime;
	
	/**
	 * 上传用户/所属用户
	 */
	@Column(name="f_uid", nullable=false, columnDefinition="int(11) COMMENT '上传用户/所属用户'")
	private Integer uid;
	
	@Transient
	private String username;
	
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Course() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLearnStages() {
		return learnStages;
	}

	public void setLearnStages(String learnStages) {
		this.learnStages = learnStages;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "Course [name=" + name + ", learnStages=" + learnStages + ", subject=" + subject + ", classes=" + classes
				+ ", version=" + version + ", createTime=" + createTime + ", uid=" + uid
				+ ", id=" + id + "]";
	}
}
