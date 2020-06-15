package com.art.manager.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * This would simply only register the springSecurityFilterChain Filter
 * for every URL in your application
 * 开启spring security的过滤器支持
 * @author Administrator
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
}
