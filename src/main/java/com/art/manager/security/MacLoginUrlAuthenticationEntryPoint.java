package com.art.manager.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.*;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 集成spring security时，因为是前后端分离，所以不能跳转到登陆页面，而是返回未登陆的JSON串。
 *
 * @author Administrator
 */
public class MacLoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint,
		InitializingBean {

	// ~ Instance fields //
	private PortMapper portMapper = new PortMapperImpl();
	private PortResolver portResolver = new PortResolverImpl();
	private String loginFormUrl;
	private boolean forceHttps = false;
	private boolean useForward = false;
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	/**
	 * 构造方法
	 *
	 * @param loginFormUrl
	 */
	public MacLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		Assert.notNull(loginFormUrl, "loginFormUrl cannot be null");
		this.loginFormUrl = loginFormUrl;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(StringUtils.hasText(loginFormUrl) && UrlUtils.isValidRedirectUrl(loginFormUrl), "loginFormUrl must be specified and must be a valid redirect URL");
		if (useForward && UrlUtils.isAbsoluteUrl(loginFormUrl)) {
			throw new IllegalArgumentException("useForward must be false if using an absolute loginFormURL");
		}
		Assert.notNull(portMapper, "portMapper must be specified");
		Assert.notNull(portResolver, "portResolver must be specified");


	}

	/**
	 * Allows subclasses to modify the login form URL that should be applicable for a
	 * given request.
	 *
	 * @param request   the request
	 * @param response  the response
	 * @param exception the exception
	 * @return the URL (cannot be null or empty; defaults to {@link #getLoginFormUrl()})
	 */
	protected String determineUrlToUseForThisRequest(HttpServletRequest request,
													 HttpServletResponse response,
													 AuthenticationException exception) {
		return getLoginFormUrl();
	}

	private static final Log logger = LogFactory
			.getLog(MacLoginUrlAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();

	}

	protected String buildRedirectUrlToLoginPage
			(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
		String loginForm = determineUrlToUseForThisRequest(request, response, authException);
		if (UrlUtils.isAbsoluteUrl(loginForm)) {
			return loginForm;
		}
		int serverPort = portResolver.getServerPort(request);
		String scheme = request.getScheme();
		RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
		urlBuilder.setScheme(scheme);
		urlBuilder.setServerName(request.getServerName());
		urlBuilder.setPort(serverPort);
		urlBuilder.setContextPath(request.getContextPath());
		urlBuilder.setPathInfo(loginForm);
		if (forceHttps && "http".equals(scheme)) {
			Integer httpsPort = portMapper.lookupHttpsPort(Integer.valueOf(serverPort));
			if (httpsPort != null) {
				// Overwrite scheme and port in the redirect URL urlBuilder.setScheme("https");
				urlBuilder.setPort(httpsPort.intValue());
			} else {
				logger.warn("Unable to redirect to HTTPS as no port mapping found for HTTP port " + serverPort);
			}
		}
		return urlBuilder.getUrl();

	}

	protected String buildHttpsRedirectUrlForRequest(HttpServletRequest request)
			throws IOException, ServletException {
		int serverPort = portResolver.getServerPort(request);
		Integer httpsPort = portMapper.lookupHttpsPort(Integer.valueOf(serverPort));

		if (httpsPort != null) {
			RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
			urlBuilder.setScheme("https");
			urlBuilder.setServerName(request.getServerName());
			urlBuilder.setPort(httpsPort.intValue());
			urlBuilder.setContextPath(request.getContextPath());
			urlBuilder.setServletPath(request.getServletPath());
			urlBuilder.setPathInfo(request.getPathInfo());
			urlBuilder.setQuery(request.getQueryString());
			return urlBuilder.getUrl();
		}
		// Fall through to server-side forward with warning message logger.warn("Unable to redirect to HTTPS as no port mapping found for HTTP port " + serverPort);
		return null;
	}


	public void setForceHttps(boolean forceHttps) {
		this.forceHttps = forceHttps;
	}

	protected boolean isForceHttps() {
		return forceHttps;
	}

	public String getLoginFormUrl() {
		return loginFormUrl;
	}

	public void setPortMapper(PortMapper portMapper) {
		Assert.notNull(portMapper, "portMapper cannot be null");
		this.portMapper = portMapper;
	}

	protected PortMapper getPortMapper() {
		return portMapper;
	}

	public void setPortResolver(PortResolver portResolver) {
		Assert.notNull(portResolver, "portResolver cannot be null");
		this.portResolver = portResolver;
	}

	protected PortResolver getPortResolver() {
		return portResolver;
	}

	/**
	 * Tells if we are to do a forward to the {@code loginFormUrl} using the
	 * * {@code RequestDispatcher},
	 * instead of a 302 redirect.
	 * * * @param useForward true if a forward to the login page should be used. Must be false *
	 * (the default) if {@code loginFormUrl} is set to an absolute value.
	 */
	public void setUseForward(boolean useForward) {
		this.useForward = useForward;
	}

	protected boolean isUseForward() {
		return useForward;
	}

}
