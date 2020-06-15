package com.art.manager.service;

import com.art.manager.pojo.SysRole;

import java.util.List;

/**
 * 查询角色的业务
 * @author Administrator
 */
public interface SysRoleService {

	/**
	 * 查询所有角色（除admin外）
	 * @return
	 */
	List<SysRole> getRoleNorAdmin();
}
