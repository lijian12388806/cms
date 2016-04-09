/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-05-04
 */
package com.xkw.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * 对所以当前在线访问的所有用户的信息封装实体
 * @author anonymous
 *
 */

public class OnLineCount {
	
	private Map<String, OnLineUser> userMap = new HashMap<>();

	public OnLineCount() {
		super();
	}

	public Map<String, OnLineUser> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, OnLineUser> userMap) {
		this.userMap = userMap;
	}
	
	public void addUser(HttpSession session, String username) {
		OnLineUser user = userMap.get(username);
		if(user==null) {
			user = new OnLineUser(username);
		}
		user.addUser(session);
		userMap.put(username, user);
	}

	public void removeUser(HttpSession session, String username) {
		OnLineUser user = userMap.get(username);
		if(user!=null) {
			user.removeUser(session);
			userMap.put(username, user);
			if(user.getUserCount()==0) {
				userMap.remove(username);
			}
		}
	}
	
	public int getTotalCount() {
		return userMap.size();
	}

}
