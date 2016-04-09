package com.xkw.mc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 素材的封装类
 * @author anonymous
 */
@Entity
@Table(name="t_source")
public class Source extends SuperEntity {

	private static final long serialVersionUID = -4515691845281183971L;

	/**
	 * 素材的类型:枚举
	 * @author anonymous
	 *
	 */
	public enum SourceTypes {
		IMAGE,//图片 
		VOICE,//音频
		VIDEO,//视频
		GROUP //素材组/文件夹
	}
	
	/**
	 * 素材名称
	 */
	@Column(name="f_name", nullable=false, columnDefinition="varchar(100) COMMENT '素材名称'")
	private String name;
	
	/**
	 * 素材访问地址
	 */
	@Column(name="f_path", nullable=false, columnDefinition="varchar(200) COMMENT '素材访问地址'")
	private String path;
	
	/**
	 * 审核状态
	 * 0:未审核
	 * 1:审核不通过
	 * 2:审核通过
	 */
	@Column(name="f_status", nullable=false, columnDefinition="int(3) DEFAULT 0 COMMENT '审核状态'")
	private Integer status;
	
	/**
	 * 素材上传时间
	 */
	@Column(name="f_createTime", nullable=false, columnDefinition="bigint(20) COMMENT '素材上传时间'")
	private Long createTime;
	
	/**
	 * 素材所属用户
	 */
	@Column(name="f_uid", nullable=false, columnDefinition="int(11) COMMENT '素材所属用户'")
	private Integer uid;
	
	/**
	 * 素材类型:枚举
	 * 数据库中存储为字符串
	 * 素材的组就是此属性为GROUP时
	 */
	@Column(name="f_type", nullable=false, columnDefinition="varchar(20) COMMENT '素材类型'")
	@Enumerated(EnumType.STRING)
	private SourceTypes type;

	/**
	 * 当前素材所属的父类id
	 * 此属性用于组中所包含的关系所用
	 * 不在组的中素材此属性值即为0
	 */
	@Column(name="f_pid", nullable=false, columnDefinition="int(11) DEFAULT 0 COMMENT '当前素材所属的父类id'")
	private Integer pid;
	
	/**
	 * 当前素材所属课件的id
	 */
	@Column(name="f_courseId", nullable=false, columnDefinition="int(11) DEFAULT 0 COMMENT '当前素材所属课件的id'")
	private Integer courseId;
	
	@Transient
	private String username;
	
	public Source() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public SourceTypes getType() {
		return type;
	}

	public void setType(SourceTypes type) {
		this.type = type;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "Material [name=" + name + ", path=" + path + ", status=" + status + ", createTime=" + createTime
				+ ", uid=" + uid + ", type=" + type + ", pid=" + pid + ", id=" + id + "]";
	}
}
