package com.art.manager.controller;


import com.art.manager.pojo.Msg;
import com.art.manager.pojo.wechat.ReceiveAddress;
import com.art.manager.request.ReceiveAddressReq;
import com.art.manager.service.ReceiveAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/address")
public class ReceiveAddressController {

    @Autowired
    private ReceiveAddressService receiveAddressService;


    /**
     * 新增收货地址
     * @param token
     * @param receiveAddress
     * @param request
     * @return
     */
    @RequestMapping(value = "/addAdress", method = RequestMethod.POST)
    public Msg addAdress(@RequestHeader(value = "Authorization") String token, @RequestBody @Valid ReceiveAddress receiveAddress, HttpServletRequest request){
        /*WeChatUser user = (WeChatUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            return receiveAddressService.insertAddress(receiveAddress);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    /**
     * 修改收货地址
     * @param token
     * @param receiveAddress
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateAdress", method = RequestMethod.POST)
    public Msg updateAdress(@RequestHeader(value = "Authorization") String token, @RequestBody ReceiveAddress receiveAddress, HttpServletRequest request){
        /*WeChatUser user = (WeChatUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            return receiveAddressService.updateAddress(receiveAddress);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    /**
     * 删除收货地址
     * @param token
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteAdress", method = RequestMethod.POST)
    public Msg deleteAdress(@RequestHeader(value = "Authorization") String token, @RequestBody ReceiveAddressReq req, HttpServletRequest request){
        /*WeChatUser user = (WeChatUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            return receiveAddressService.deleteAdress(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    /**
     * 设置收货地址为默认收货地址
     * @param token
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateAddressStatus", method = RequestMethod.POST)
    public Msg updateAddressStatus(@RequestHeader(value = "Authorization") String token, @RequestBody ReceiveAddressReq req, HttpServletRequest request){
        /*WeChatUser user = (WeChatUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            return receiveAddressService.updateAddressStatus(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    /**
     * 查询收货地址列表
     * 第一条数据是默认收货地址
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectAddressList", method = RequestMethod.POST)
    public Msg selectAddressList(@RequestHeader(value = "Authorization") String token,@RequestBody ReceiveAddressReq req, HttpServletRequest request){
        /*WeChatUser user = (WeChatUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            return receiveAddressService.selectAddressList(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }
}
