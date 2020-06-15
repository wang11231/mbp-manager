package com.art.manager.service;

import com.art.manager.pojo.SysLoginLog;

/**
 * 记录登录日志
 * @author Administrator
 */
public interface SysLoginLogService {

	/**
	 * 添加一条登录的日志记录
	 * @param log
	 * @return
	 */
	int addLoinLog(SysLoginLog log);
}
