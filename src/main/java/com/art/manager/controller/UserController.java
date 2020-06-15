package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.SysMenu;
import com.art.manager.pojo.SysUser;
import com.art.manager.request.SysUserReq;
import com.art.manager.util.JsonUtil;
import com.art.manager.vo.SysUserVo;
import com.art.manager.service.SysUserService;
import com.art.manager.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户控制类
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class
UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final Long TOKEN_TIMEOUT = 60L;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ResponseBody
    @PostMapping("/register")
    public Msg register(@RequestBody @Valid SysUser user) {
        try {
            SysUser sysUser = sysUserService.register(user);
            return new Msg(Msg.SUCCESS_CODE, "注册成功", new Date(), sysUser.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/delete")
    public Msg delete(@RequestBody SysUserReq users) {
        try {
            if(users == null ||users.getIds() == null || users.getIds().length== 0){
                return new Msg(Msg.FAILURE_CODE, "用户id为空");
            }
            boolean result = sysUserService.delete(users.getIds());
            if(result){
                return new Msg(Msg.SUCCESS_CODE, "删除成功");
            }else{
                return new Msg(Msg.FAILURE_CODE, "删除失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updatePassword")
    public Msg updatePassword(@RequestBody SysUser user) {
        try {
            boolean result = sysUserService.updatePassword(user);
            if(result){
                return new Msg(Msg.SUCCESS_CODE, "修改成功");
            }else{
                return new Msg(Msg.FAILURE_CODE, "修改失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateUserRoles")
    public Msg updateUserRoles(@RequestBody SysUser user) {
        try {
            boolean result = sysUserService.updateUserRoles(user);
            if(result){
                return new Msg(Msg.SUCCESS_CODE, "修改成功");
            }else{
                return new Msg(Msg.FAILURE_CODE, "修改失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getListByPage")
    public Msg getListByPage(@RequestBody @Valid SysUserReq req) {
        try {

            Map<String, Object> map = sysUserService.getListByPage(req);
            return new Msg(Msg.SUCCESS_CODE, map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public Msg login(@RequestBody SysUser sysUser, HttpServletRequest request) {
        if(StringUtils.isBlank(sysUser.getUsername())){
            return new Msg(Msg.FAILURE_CODE, "用户名为空");
        }
        if(StringUtils.isBlank(sysUser.getPassword())){
            return new Msg(Msg.FAILURE_CODE, "密码为空");
        }
        try {
            UserDetails user = sysUserService.authenticate(sysUser);
            /**
             * 将登陆的用户信息存放到session中
             */
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String encode = passwordEncoder.encode(sysUser.getUsername());
            String token = MD5Utils.md5(encode);
            HttpSession session = request.getSession();
            session.setAttribute(token, user);
            //保存redis
            SysUserVo res = new SysUserVo();
            BeanUtils.copyProperties(user, res);
            res.setToken(token);
            res.setId(((SysUser) user).getId());
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(token, JsonUtil.objectTojson(res), TOKEN_TIMEOUT, TimeUnit.MINUTES);
            log.info("sessionId:{}", session.getId());
            return new Msg(Msg.SUCCESS_CODE, res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    @ResponseBody
    @PostMapping("/getUserMenus")
    public Msg getUserMenus(@RequestBody SysUser sysUser) {

        try {
            List<SysMenu> menus = sysUserService.getUserMenus(sysUser);
            return new Msg(Msg.SUCCESS_CODE, menus);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    @ResponseBody
    @PostMapping("/logout")
    public Msg logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                return new Msg(Msg.FAILURE_CODE, "用户未登录", new Date());
            }
            session.invalidate();
            Msg msg = new Msg(Msg.SUCCESS_CODE, "成功退出", new Date());
            return msg;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    @ResponseBody
    @PostMapping("/getCountByUsername")
    public Msg getCountByUsername(@RequestBody SysUser sysUser) {
        try {
            return new Msg(Msg.SUCCESS_CODE, sysUserService.getCountByUsername(sysUser));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }




}
