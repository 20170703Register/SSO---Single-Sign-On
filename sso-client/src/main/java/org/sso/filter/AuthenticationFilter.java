package org.sso.filter;


import org.sso.utils.CodeUtils;
import org.sso.utils.HTTPUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * 认证过滤器
 **/
public class AuthenticationFilter implements Filter {


    private static String serviceURL;                                          // 认证中心的URL

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.serviceURL = filterConfig.getInitParameter("serviceURL");      // 获取认证中心的URL
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURL().toString();
        HttpSession session = request.getSession();
        String isLogin = (String) session.getAttribute("loginFlag");        // 获取登录标识（局部会话）

        // 对于用户已登录，首页请求，登出请求，认证中心的登出请求放行
        if (isLogin != null
                || url.indexOf("index") != -1
                || url.indexOf("logout") != -1
                || url.indexOf("serviceLogout") != -1){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = request.getParameter("token");                        // 如果能获取到Token，说明用户在认证中登录了，并且得到了认证中发放的Token
        if (token != null){
            String sessionId = session.getId();
            String projectName = request.getContextPath();
            String clientUrl = url.substring(0, url.indexOf(projectName) + projectName.length());
            String username = HTTPUtils.request(serviceURL + "/token" +
                    "?client="+ CodeUtils.url(clientUrl) +
                    "&token=" + CodeUtils.url(token) +
                    "&sessionId=" + sessionId);                                 // 请求认证中心校验Token是否有效

            if (!"Not".equals(username) && username != null) {
                session.setAttribute("loginFlag", token);                    // Token是有效，创建局部会话，标记用户在本系统中登录
                session.setAttribute("username", username);                  // Token是有效，得到用户名
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        String loginURL = serviceURL + "?client=" + CodeUtils.url(url);
        response.sendRedirect(loginURL);                                        // 未登录，重定向到SSO认证中心
    }

    @Override
    public void destroy() {

    }
}
