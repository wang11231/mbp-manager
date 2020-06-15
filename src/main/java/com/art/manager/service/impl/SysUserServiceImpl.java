package com.art.manager.service.impl;

import com.art.manager.enums.StatusEnum;
import com.art.manager.exception.UserException;
import com.art.manager.mapper.SysRoleMapper;
import com.art.manager.mapper.SysUserMapper;
import com.art.manager.mapper.UserRoleMapper;
import com.art.manager.pojo.*;
import com.art.manager.request.SysUserReq;
import com.art.manager.service.SysUserService;
import com.art.manager.util.EncryptUtil;
import com.art.manager.vo.SysRoleVo;
import com.art.manager.vo.SysUserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;*/
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author snow
 */
@EnableTransactionManagement
@Service
public class SysUserServiceImpl implements SysUserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    /*@Autowired
    private RedisTemplate redisTemplate;*/
    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andStatusEqualTo(StatusEnum.VALID.getCode());
        List<SysUser> sysUsers = userMapper.selectByExample(example);
        if (sysUsers == null || sysUsers.size() == 0) {
            throw new UserException("用户名不存在！");
        }
        return sysUsers.get(0);
    }


    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public int deleteByPrimaryKey(Long id) {

        //int rowRole = roleMapper.deleteByUserId(id);
        int rowUser = userMapper.deleteByPrimaryKey(id);
        return 0;
    }


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int deleteByExample(SysUserExample example) {
        List<SysUser> users = findByExample(example);
        if (users == null || users.size() == 0 || users.size() > 1) {
            //返回0 表示传入的条件不正确
            return 0;
        }
        SysUser user = users.get(0);

        return deleteByPrimaryKey(user.getId());
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int updateByExample(SysUser user, SysUserExample example) {
        return userMapper.updateByExample(user, example);
    }

    @Override
    public List<SysUser> findAll() {
        return userMapper.selectByExample(null);
    }

    @Override
    public List<SysUser> findByExample(SysUserExample example) {
        return userMapper.selectByExample(example);
    }

    @Override
    public SysUser selectByPrimaryKey(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public UserDetails authenticate(SysUser sysUser) {
        //校验用户
        UserDetails user = loadUserByUsername(sysUser.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(sysUser.getPassword(), user.getPassword());
        if (!matches) {
            throw new UserException("密码错误！");
        }
        return user;
    }

    @Override
    public List<SysMenu> getUserMenus(SysUser sysUser){
        if(sysUser == null || sysUser.getId() == null){
            throw new UserException("用户id为空");
        }
        //获取权限
        UserRole userRole = new UserRole();
        userRole.setUserId(sysUser.getId());
        List<SysMenu> menus = userRoleMapper.selectMenuByUser(userRole);
        //拼装角色上下级关系
        List<SysMenu> userMenus = new ArrayList<>();
        if(menus != null && menus.size() > 0){
            for(SysMenu menu : menus){
                Integer parentId = menu.getParentId();
                if(parentId != null){
                    Iterator<SysMenu> iterator = userMenus.iterator();
                    while(iterator.hasNext()){
                        SysMenu next = iterator.next();
                        if(next.getId().equals(parentId)){
                            next.getMenus().add(menu);
                        }
                    }
                }else{
                    userMenus.add(menu);
                }
            }
        }
        return userMenus;
    }

    @Override
    public boolean getCountByUsername(SysUser sysUser) {
        if(StringUtils.isBlank(sysUser.getUsername())){
            throw new UserException("用户名为空");
        }
        int count = userMapper.getCountByUsername(sysUser);
        return count == 0 ? false : true;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public SysUser register(SysUser user) {
        //插入用户信息
        user.setPassword(EncryptUtil.encode(user.getPassword()));
        int insert = insert(user);
        if (insert < 1) {
            throw new UserException("注册失败");
        }
        Long userId = user.getId();
        logger.debug("插入用户表记录的主键id为：" + userId);
        //插入用户角色
        insertUserRoles(userId, user.getRoles());
        return user;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public SysUser registerBack(SysUser user) {

        user.setPassword(EncryptUtil.encode(user.getPassword()));

        user.setStatus(1);
        int insert = insert(user);
        if (insert < 1) {
            throw new RuntimeException("注册失败");
        }
        Long userId = user.getId();
        logger.debug("插入用户表记录的主键id为：" + userId);

        if (userId < 1) {
            throw new RuntimeException("注册失败");
        }
        // 往商户表中插入一条记录
        return user;
    }


    @Override
    public int insert(SysUser user) {
        return userMapper.insertSelective(user);
    }

    @Override
    public int insertBatch(SysUser user, List<SysRole> roleList) {
        return 0;
        //return userMapper.insertBatch(user, roleList);
    }


    @Override
    public List<SysUser> searchUserAndRolesByCondition(SysUser user, Integer roleId) {

        logger.debug(roleId + "...........................");
        List<Integer> userIds = null;
        if (roleId != null && roleId != 0) {
            //查询该觉得对应的所有用户的id
           // userIds = roleMapper.selectUserIdsByRoleId(roleId);
        }

       // return userMapper.searchUserAndRolesByCondition(user, userIds);
        return null;
    }

    @Override
    public int updateByPrimaryKey(SysUser user) {

        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public int updateBySelective(SysUser user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int updateUserInfo(SysUser user) {

        int updateUser = userMapper.updateByPrimaryKeySelective(user);
        return 0;
    }

    @Override
    public List<SysUser> searchAllUserAndRoles() {


        return null;
    }

    @Override
    public int updateUserStatus(SysUser user) {


        return 0;
    }



    @Override
    public int disableOrEnableUser(SysUser user) {


        /*Integer status = user.getStatus();
        if (status.equals(0)) {
            //如果用户状态是0，表示要冻结账户
            //从redis里面将用户的session移除掉
            HashOperations operations = redisTemplate.opsForHash();
            Object o = operations.get("user_tokens", user.getUsername());

            String sessionPre = "spring:session:sessions:";
            String sessionId = sessionPre + o.toString();
            logger.info("要删除的sessionId为：" + sessionId);
            Boolean deleteSession = redisTemplate.delete(sessionId);

            logger.info("是否将redis中的session清除：" + deleteSession);

            //从redis里面将用户的expires移除掉
            String expiresPre = "spring:session:sessions:expires:";
            String expires = expiresPre + o.toString();
            logger.info("要删除的expires为：" + expires);
            Boolean deleteExpires = redisTemplate.delete(expires);

            logger.info("是否将redis中的expires清除：" + deleteExpires);

            //将redis中的user_tokens移除
            Long deleteTokens = operations.delete("user_tokens", user.getUsername());
            logger.info("从redis中移除了用户名为" + user.getUsername() + "的user_tokens" + deleteTokens + "个");
        }
        user.setUsername(null);*/
        return userMapper.updateByPrimaryKeySelective(user);
    }


    @Override
    public boolean isRegister(SysUser user) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        if (user.getUsername() != null && user.getUsername().length() > 0) {
            criteria.andUsernameEqualTo(user.getUsername());
        }
        if (user.getTelephone() != null && user.getTelephone().length() > 0) {
            criteria.andTelephoneEqualTo(user.getTelephone());
        }
        if (user.getEmail() != null && user.getEmail().length() > 0) {
            criteria.andEmailEqualTo(user.getEmail());
        }
        return userMapper.countByExample(example) == 0 ? false : true;
    }

    private boolean isRegister(String param) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andTelephoneEqualTo(param);
        long row = userMapper.countByExample(example);

        if (row == 1) {
            return true;
        }

        return false;
    }


    @Override
    public boolean resetPassword(String oldPassword, SysUser user) {
		/*if(this.saveUser == null || oldPassword == null){
			return false;
		}*/
        SysUser saveUser = selectByPrimaryKey(user.getId());

        boolean matches = EncryptUtil.matches(oldPassword, saveUser.getPassword());
        if (!matches) {

            logger.debug("原密码错误...");

            return matches;
        }
        return resetPass(user);
    }

    /**
     * create by:   CH
     * create time: 20:35 2018/6/25
     * 个人中心信息
     */
    @Override
    public List<SysUser> merchantCenter(SysUserExample example) {
        return userMapper.selectByExample(example);
    }


    private boolean resetPass(SysUser user) {
        Long id = user.getId();
        if (id == null) {
            SysUser sysUser = findByUsername(user.getUsername());
            user.setId(sysUser.getId());
        }
        user.setPassword(EncryptUtil.encode(user.getPassword()));
        try {
            userMapper.updateByPrimaryKeySelective(user);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.debug("修改密码失败：用户的用户名为：" + user.getUsername());
            return false;
        }
    }

    @Override
    public List<SysUser> selectImperfectUsers(Map<String, Object> map) {

        //return userMapper.selectImperfectUsers(map);
        return null;
    }

    @Override
    public SysUser findByUsername(String username) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<SysUser> users = userMapper.selectByExample(example);
        if (users == null || users.size() == 0) {
            return null;
        }
        if (users.size() > 1) {
            throw new RuntimeException("同一个手机号两个用户");
        }
        return users.get(0);
    }

    @Override
    public void resetPassword(SysUser user) {

        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long[] ids) {
        if(ids == null || ids.length == 0){
            throw new UserException("用户id为空！");
        }
        for(int i=0; i<ids.length; i++){
            deleteOne(ids[i]);
        }
        return true;
    }

    private boolean deleteOne(Long userId){
        //删除用户信息
        SysUser user = new SysUser();
        user.setId(userId);
        user.setStatus(StatusEnum.INVALID.getCode());
        //判断是否是管理员
        int count = userMapper.getAdminCount(user);
        if(count > 0){
            throw new UserException("管理员用户不能删除");
        }
        int result = userMapper.updateByPrimaryKeySelective(user);
        //删除用户权限
        if(result > 0){
            deleteRolesByUser(userId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(SysUser sysUser) {
        Long userId = sysUser.getId();
        if(userId == null){
            throw new UserException("用户id为空");
        }
        sysUser.setUsername(null);//用户名不能为空
        String password = sysUser.getPassword();
        if(StringUtils.isBlank(password)){
           throw new UserException("用户密码为空");
        }
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(EncryptUtil.encode(password));
        int result = userMapper.updateByPrimaryKeySelective(user);
        return result > 0 ? true :false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRoles(SysUser user){
        Long userId = user.getId();
        if(userId == null){
            throw new UserException("用户id为空");
        }
        Long[] userRoles = user.getRoles();
        if(userRoles == null || userRoles.length == 0){
            throw new UserException("用户角色为空");
        }
        //删除
         deleteRolesByUser(userId);
        //插入
        insertUserRoles(userId, userRoles);
        return true;
    }

    @Override
    public Map<String, Object> getListByPage(SysUserReq req) {
        //查询所有用户
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysUser> users = userMapper.getList(req);
        Map<String, Object> result = new HashMap();
        List<SysUserVo> userList = new ArrayList<>();
        if(users == null || users.size() == 0){
            result.put("total", 0);
            result.put("pages", 0);
            result.put("list", userList);
            return result;
        }
        PageInfo<SysUser> pageInfo = new PageInfo<>(users);
        //查询用户对用的权限
        List<UserRole> roles = userRoleMapper.selectRolesByList(users);
        int pages = Page.getPages(pageInfo.getTotal(), req.getPageSize());
        //组装用户权限

        for(SysUser user : users){
            if(roles != null && roles.size() > 0){
                for(UserRole role : roles){
                    if(user.getId().equals(role.getUserId())){
                        SysRoleVo sysRoleVo= new SysRoleVo();
                        BeanUtils.copyProperties(role, sysRoleVo);
                        user.getUserRoles().add(sysRoleVo);
                    }
                }
            }
            SysUserVo sysUserVo = new SysUserVo();
            BeanUtils.copyProperties(user, sysUserVo);
            userList.add(sysUserVo);
        }
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", userList);
        return result;
    }

    private int insertUserRoles(Long userId, Long[] roles){
        if(roles != null && roles.length > 0){
            List<UserRole> list =new ArrayList();
            for(int i=0; i<roles.length; i++){
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roles[i]);
                list.add(userRole);
            }
            return userRoleMapper.batchInsertRoles(list);
        }
        return 0;
    }

    private int deleteRolesByUser(Long userId){
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setStatus(StatusEnum.INVALID.getCode());
        return userRoleMapper.deleteRolesByUser(userRole);
    }

}
