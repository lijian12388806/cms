/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-09
 */
package com.xkw.mc.component.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xkw.mc.entity.AdminUser;
import com.xkw.mc.entity.Authority;
import com.xkw.mc.entity.Role;
import com.xkw.mc.service.AdminUserService;



/**
 * 登录验证
 * @version 1.0
 * @author LiaoGang
 */

@Component("userDetailsService")
public class McUserDetailsService implements UserDetailsService ,Serializable{
	private static final long serialVersionUID = 2735743264777125232L;
	
	@Autowired
	private AdminUserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		AdminUser user = userService.getByProperty("username", username);
		if(user==null) {
			throw new UsernameNotFoundException("用戶名不存在!");
		}
		
		String password = user.getPassword();
		boolean enabled = user.getEnabled();
		if(enabled) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			session.setMaxInactiveInterval(15*60);
		}
		
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		Collection<GrantedAuthority> authorities = new LinkedHashSet<>();
		
		Role role = user.getRole();
		if(role!=null && role.getEnabled()) {
			//获取该用户所具有的所有权限,然后添加到springsecurity框架中
			for(Authority authority : role.getAuthorities()){
				authorities.add(new GrantedAuthorityImpl(authority.getRoleName()));
			}
		}
		
		SecurityUser securityUser = new SecurityUser(username, password, enabled,
			accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, user);
		
		return securityUser;
	}
	
	public class SecurityUser extends org.springframework.security.core.userdetails.User {
		private static final long serialVersionUID = 5031099308611338175L;
		private AdminUser user;
		public AdminUser getUser() {
			return user;
		}
		public SecurityUser(String userName, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities,
			AdminUser user) {
			
			super(userName, password, enabled, accountNonExpired, credentialsNonExpired,
					accountNonLocked, authorities);
			this.user = user;
		}
	}
}
