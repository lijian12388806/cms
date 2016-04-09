package com.xkw.mc.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 权限的封装类
 * @author anonymous
 *
 */
@Entity
@Table(name="t_authority")
public class Authority extends SuperEntity {

	private static final long serialVersionUID = -183647072937679415L;

	/**
	 * 权限显示的名称
	 */
	@Column(name="f_displayName", unique=true, nullable=false, columnDefinition="varchar(30) COMMENT '权限显示的名称'")
	private String displayName;
	
	/**
	 * 权限名称,用于加入springsecurity中, 须以ROLE_开头
	 */
	@Column(name="f_roleName", unique=true, nullable=false, columnDefinition="varchar(30) COMMENT '权限名称,用于加入springsecurity中, 须以ROLE_开头'")
	private String roleName;
	
	/**
	 * 当前权限的父权限
	 */
	@ManyToOne
	@JoinColumn(name="f_parent_id")
	private Authority parentAuthority;
	
	/**
	 * 当前权限的子权限的集合
	 */
	@OneToMany(mappedBy="parentAuthority")
	private Set<Authority> subAuthorities ;
	
	/**
	 * 当前权限所拦截的资源
	 */
	@OneToMany(mappedBy="authority", cascade={CascadeType.ALL})
	private Set<Resource> resources = new HashSet<>();


	public Authority() {
		super();
	}


	public Authority(Integer id) {
		super();
		this.id = id;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public Authority getParentAuthority() {
		return parentAuthority;
	}


	public void setParentAuthority(Authority parentAuthority) {
		this.parentAuthority = parentAuthority;
	}


	public Set<Authority> getSubAuthorities() {
		return subAuthorities;
	}


	public void setSubAuthorities(Set<Authority> subAuthorities) {
		this.subAuthorities = subAuthorities;
	}


	public Set<Resource> getResources() {
		return resources;
	}

	
	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}
	
	
	/**
	 * 获取当前权限所拦截的url
	 * @return
	 */
	@Transient
	public List<String> getAuthResources(){
		List<String> resourceStr = new ArrayList<>();
		for(Resource resource : this.resources){
			resourceStr.add("" + resource.getUrl());
		}
		return resourceStr;
	}
	
}
