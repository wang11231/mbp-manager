package com.art.manager.service;

import com.art.manager.pojo.*;
import com.art.manager.request.SysUserReq;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

/**
 * 用户的业务逻辑
 * @author Administrator
 */
public interface SysUserService {

	/**
	 * 根据主键id删除用户
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * 根据多个字段删除用户
	 * @param example
	 * @return
	 */
	int deleteByExample(SysUserExample example);

	/**
	 * 更新用户
	 * @param user
	 * @param example
	 * @return
	 */
	int updateByExample(SysUser user, SysUserExample example);

	/**
	 * 查询所有用户
	 * @return
	 */
	List<SysUser> findAll();


	/**
	 * 条件查询
	 * @return
	 */
	List<SysUser> findByExample(SysUserExample example);

	/**
	 * 根据主键查询用户
	 * @param id
	 * @return
	 */
	SysUser selectByPrimaryKey(Long id);

	/**
	 * 验证用户名和密码
	 * @param loginUser
	 * @return
	 */
	UserDetails authenticate(SysUser loginUser);

	/**
	 * 注册一个用户,并将角色信息也注册到数据库
	 * @param user
	 * @return 返回该用户，带上主键id
	 */
	SysUser register(SysUser user);

	/**
	 * 注册后台用户,并将角色信息也注册到数据库
	 * @param user
	 * @return 返回该用户，带上主键id
	 */
	SysUser registerBack(SysUser user);

	/**
	 * 添加一个用户，只添加用户信息，不添加关联的角色信息
	 * @param user
	 * @return
	 */
	int insert(SysUser user);

	/**
	 * 将用户关联的角色信息插入到数据库中
	 * @param user
	 * @param roleList
	 * @return
	 */
	int insertBatch(SysUser user, List<SysRole> roleList);


	/**
	 * 条件查询用户
	 * @param user
	 * @param roleId
	 * @return
	 */
	List<SysUser> searchUserAndRolesByCondition(SysUser user, Integer roleId);

	/**
	 * 根据主键更新用户信息
	 * @param user
	 * @return
	 */
	int updateByPrimaryKey(SysUser user);


	/**
	 * 根据主键更新用户部分信息
	 * @param user
	 * @return
	 */
	int updateBySelective(SysUser user);

	/**
	 * 更新用户的信息包括角色信息
	 * @param user
	 * @return
	 */
	int updateUserInfo(SysUser user);

	/**
	 * 查询后台的用户和角色
	 * @return
	 */
	List<SysUser> searchAllUserAndRoles();

	/**
	 * 根据id更新用户的状态
	 * 0：冻结  1：激活
	 * @param user
	 * @return
	 */
	int updateUserStatus(SysUser user);

	/**
	 * 冻结或激活用户
	 * @param user
	 * @return
	 */
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	int disableOrEnableUser(SysUser user);

	/**
	 * 判断用户名，手机号，邮箱是否注册
	 * @param user
	 * @return
	 */
	boolean isRegister(SysUser user);


	/**
	 * 改方法对外调用
	 * 修改用户的密码：自己修改自己的密码
	 * @param sysUser
	 * @param oldPassword
	 * @return true：修改成功，false：修改失败
	 */
	boolean resetPassword(String oldPassword, SysUser sysUser);

	/**
	 * 商户中心
	 * @param example
	 * @return
	 */
	List<SysUser> merchantCenter(SysUserExample example);

	/**
	 * 查询未完善的已经注册的商户信息
	 * @param map 条件查询
	 * @return
	 */
	List<SysUser> selectImperfectUsers(Map<String, Object> map);

	/**
	 *
	 * 根据用户名查询用户
	 * @param username
	 *
	 * @return
	 */
	SysUser findByUsername(String username);


	/**
	 * 更新密码
	 * @param user
	 */
	void resetPassword(SysUser user);

	/**
	 * 删除用户
	 * @param ids
	 * @return
	 */
	boolean delete(Long[] ids);

	/**
	 * 修改用户密码
	 * @param sysUser
	 * @return
	 */
	boolean updatePassword(SysUser sysUser);

	/**
	 * 分页查询用户
	 * @param req
	 * @return
	 */
	Map<String, Object> getListByPage(SysUserReq req);

	/**
	 * 查询用户菜单
	 * @param sysUser
	 * @return
	 */
	List<SysMenu> getUserMenus(SysUser sysUser);

	/**
	 * 查询用户菜单
	 * @param sysUser
	 * @return
	 */
	boolean getCountByUsername(SysUser sysUser);

	/**
	 * 修改用户角色
	 * @param user
	 * @return
	 */
	boolean updateUserRoles(SysUser user);

}





