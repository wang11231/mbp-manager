package com.art.manager.controller;


import com.art.manager.pojo.Msg;
import com.art.manager.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色控制类
 */
@RestController
@RequestMapping("role")
@Slf4j
public class RoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询所有角色（除admin）
     * @return
     */
    @PostMapping("getRoleNorAdmin")
    public Msg getRoleNorAdmin(){
        try{
            return new Msg(Msg.SUCCESS_CODE, sysRoleService.getRoleNorAdmin());
        }catch(Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

}
