package com.art.manager.mapper;

import com.art.manager.pojo.SysMenu;
import com.art.manager.pojo.SysUser;
import com.art.manager.pojo.UserRole;
import com.art.manager.pojo.UserRoleExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRoleMapper {
    int countByExample(UserRoleExample example);

    int deleteByExample(UserRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    List<UserRole> selectByExample(UserRoleExample example);

    UserRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserRole record, @Param("example") UserRoleExample example);

    int updateByExample(@Param("record") UserRole record, @Param("example") UserRoleExample example);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    int batchInsertRoles(List<UserRole> list);

    int deleteRolesByUser(UserRole record);

    List<UserRole> selectRolesByList(List<SysUser> list);

    List<SysMenu> selectMenuByUser(UserRole record);

}