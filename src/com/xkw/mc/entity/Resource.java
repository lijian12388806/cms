package com.xkw.mc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 系统资源的封装类
 * @author Administrator
 *
 */
@Table(name="t_resource")
@Entity
public class Resource extends SuperEntity {

	private static final long serialVersionUID = 812819902001557295L;

	
	/**
	 * 受权限控制的url, 相当于WEB应用的根路径
	 */
	@Column(name="f_url", columnDefinition="varchar(300) COMMENT '受权限控制的url, 相当于WEB应用的根路径'")
	private String url;
	
	
	/**
	 * 该资源所属权限的id
	 */
	@ManyToOne
	@JoinColumn(name="f_authority_id", columnDefinition="int(11) COMMENT '该资源所属权限的id, 外键'")
	private Authority authority;


	public Resource() {
		super();
	}
	
	
	public Resource(Integer id) {
		super();
		this.id = id;
	}


	public Resource(String url) {
		this.url = url;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public Authority getAuthority() {
		return authority;
	}


	public void setAuthority(Authority authority) {
		this.authority = authority;
	}


	@Override
	public String toString() {
		return "Resource [url=" + url + ", id=" + id + "]";
	}
	
}
