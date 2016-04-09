/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-05-04
 */
package com.xkw.utils;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

/**
 * 对所以当前在线访问的用户的信息封装实体
 * 
 * @author LiaoGang
 * 
 */

public class OnLineUser {
	
	private String username;
	
	// 所有在线已登录用户用户名的集合
	private Set<HttpSession> userSet = new HashSet<>();
	
	public OnLineUser() {
		super();
	}
	
	public OnLineUser(String username) {
		super();
		this.username = username;
	}

	public OnLineUser(Set<HttpSession> userSet) {
		super();
		this.userSet = userSet;
	}

	public void addUser(HttpSession session) {
		userSet.add(session);
	}

	public void removeUser(HttpSession session) {
		userSet.remove(session);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Set<HttpSession> getUserSet() {
		return userSet;
	}

	public void setUserSet(Set<HttpSession> userSet) {
		this.userSet = userSet;
	}

	public int getUserCount() {
		return userSet.size();
	}
	
}
