package com.xkw.mc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * cms后台管理账户的信息封装类 
 * @version 1.0
 * @author LiaoGang
 */

@Entity
@Table(name="t_admin_user")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class AdminUser extends SuperEntity {
	private static final long serialVersionUID = 3903763449728617715L;
	
	/**
	 * 管理员账号/登录账号
	 */
	@Column(name="f_username", unique=true, nullable=false, columnDefinition="varchar(30) COMMENT '管理员账号/登录账号'")
	private String username;
	
	/**
	 * 登录密码
	 */
	@Column(name="f_password", nullable=false, columnDefinition="varchar(64) COMMENT '密码'")
	private String password;
	
	/**
	 * 是否可用
	 */
	@Column(name="f_enabled", nullable=false, columnDefinition="BIT DEFAULT 0  COMMENT '是否可用'")
	private Boolean enabled;
	
	
	/**
	 * 用户个人邮箱/可用于邮箱注册
	 */
	@Column(name="f_email", length=50, columnDefinition="varchar(50) COMMENT '用户个人邮箱/可用于邮箱注册'")
	private String email;
	
	
	/**
	 * 手机联系电话/可用于电话注册
	 */
	@Column(name="f_cellphone", length=15, columnDefinition="varchar(15) COMMENT '手机联系电话/可用于电话注册'")
	private String cellphone;
	
	
	/**
	 * 管理员真实姓名
	 */
	@Column(name="f_realName", length=30, columnDefinition="varchar(30) COMMENT '管理员真实姓名'")
	private String realName;
	
	
	@ManyToOne
	@JoinColumn(name="f_role_id")
	private Role role;
	
	
	public AdminUser() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}


	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return "AdminUser [username=" + username + ", password=" + password + ", enabled=" + enabled + ", email="
				+ email + ", cellphone=" + cellphone + ", realName=" + realName + "]";
	}
	
}
