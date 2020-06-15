package com.art.manager.config;

import com.art.manager.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 用户认证管理器
 * @author Administrator
 */
@Service
public class SysUserAuthenticationManager implements AuthenticationManager {

	@Autowired
	private SysUserServiceImpl sysUserService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		if(! (authentication instanceof UsernamePasswordAuthenticationToken)){
			return null;
		}
		UsernamePasswordAuthenticationToken  token = (UsernamePasswordAuthenticationToken) authentication;

		//
		Object principal = token.getPrincipal();

		//class java.lang.String
		//System.out.println(principal.getClass());
		//token.getPrincipal().toString() 拿到的就是输入的用户名
		String username = token.getPrincipal().toString();
		//class java.lang.String
		//System.out.println(token.getCredentials().getClass());
		//token.getCredentials().toString() 拿到的就是输入的密码
		//System.out.println(token.getCredentials().toString());
		UserDetails userDetails = sysUserService.loadUserByUsername(username);

		Boolean equals = equals(token, userDetails);
		if(equals){
			return authentication;
		}
		return null;

	}

	public static Boolean equals(UsernamePasswordAuthenticationToken token,UserDetails userDetails){
		if(token == null || userDetails == null){
			return false;
		}

		if(token.getPrincipal() == null && token.getCredentials() == null){
			return false;
		}
		if(userDetails.getUsername() == null && userDetails.getPassword() == null){
			return false;
		}

		if(!(token.getPrincipal().equals(userDetails.getUsername()))
				||  !(token.getCredentials().equals(userDetails.getPassword()))){

			return  false;
		}
		return true;
	}
}
