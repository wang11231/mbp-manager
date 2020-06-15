package com.art.manager.service.impl;

import com.art.manager.mapper.SysRoleMapper;
import com.art.manager.pojo.SysRole;
import com.art.manager.pojo.SysRoleExample;
import com.art.manager.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Override
	public List<SysRole> getRoleNorAdmin() {
		SysRoleExample example = new SysRoleExample();
		SysRoleExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andRoleNameNotEqualTo("admin");//去除管理员角色
		return sysRoleMapper.selectByExample(example);
	}
}
