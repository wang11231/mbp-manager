package com.art.manager.service.impl;

import com.art.manager.mapper.SysLoginLogMapper;
import com.art.manager.pojo.SysLoginLog;
import com.art.manager.service.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class SysLoginLogServiceImpl implements SysLoginLogService {

	@Autowired
	private SysLoginLogMapper sysLoginLogMapper;

	@Override
	public int addLoinLog(SysLoginLog log) {
		return sysLoginLogMapper.insertSelective(log);
	}
}
