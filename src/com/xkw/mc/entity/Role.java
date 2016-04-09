package com.xkw.mc.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 管理员角色的实体类
 * @author anonymous
 *
 */
@Entity
@Table(name="t_role")
public class Role extends SuperEntity {

	private static final long serialVersionUID = -7424982948375972583L;
	
	/**
	 * 角色显示的名称
	 */
	@Column(name="f_displayName", unique=true, nullable=false, columnDefinition="varchar(30) COMMENT '角色显示的名称'")
	private String displayName;
	
	
	/**
	 * 当前角色所包含的权限
	 */
	@ManyToMany
	@JoinTable(
			name="t_role_authority", 
			joinColumns={@JoinColumn(name="role_id")},
			inverseJoinColumns={@JoinColumn(name="authority_id")}
	)
	private Set<Authority> authorities = new HashSet<>();

	/**
	 * 当前角色是否可用, 若不可用,则相关使用该角色的用户都无角色对应的权限
	 */
	@Column(name="f_enabled", nullable=false, columnDefinition="BIT DEFAULT 0  COMMENT '当前角色是否可用'")
	private Boolean enabled;
	
	public Role() {
		super();
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setUserAuths(List<String> authorities){
		this.authorities.clear();
		for(String authorityId: authorities){
			this.authorities.add(new Authority(Integer.parseInt(authorityId)));
		}
	}
	
	@Transient
	public List<String> getUserAuths(){
		List<String> authoritesStr = new ArrayList<>();
		for(Authority authority:this.authorities){
			authoritesStr.add("" + authority.getId());
		}
		return authoritesStr;
	}
	
	@Override
	public String toString() {
		return "Role [displayName=" + displayName + ", enabled=" + enabled + ", id=" + id + "]";
	}
	
}
