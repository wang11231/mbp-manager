package com.art.manager.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Ip工具类
 */
public class IpUtils {

    /**
     * 获取request中Ip
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
         String ip = request.getHeader("x-forwarded-for");
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getHeader("Proxy-Client-IP");
             }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                 ip = request.getHeader("WL-Proxy-Client-IP");
             }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                 ip = request.getHeader("HTTP_CLIENT_IP");
             }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                 ip = request.getHeader("HTTP_X_FORWARDED_FOR");
             }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                 ip = request.getRemoteAddr();
             }
         return ip;
     }


}
