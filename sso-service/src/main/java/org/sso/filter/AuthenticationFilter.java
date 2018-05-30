package org.sso.filter;

import org.sso.utils.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;


/**
 * 认证中心拦截器
 **/
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURL().toString();
        if (url.indexOf("login") != -1
                || url.indexOf("token") != -1
                || url.indexOf("logout") != -1){
            filterChain.doFilter(servletRequest, servletResponse);              // 是登录请求，校验token请求，登出请求放行
            return;
        }else if (url.indexOf("error") != -1){
            request.getRequestDispatcher("/").forward(servletRequest, servletResponse);     // 转发到登录页面
            return;
        }

        Cookie[] cookies = request.getCookies();
        String tgc = CookieUtils.get(cookies, "TGC");                       // 获取全局会话TGC（这里的TGC就是全局会话的sessionid）
        String isLogin = (String) SessionUtils.get(tgc, "loginFlag");
        if (isLogin != null){                                                   // 如果用户已经登录，就重新发放Token到客户端
            String clientURL = request.getParameter("client");
            String token = CodeUtils.url(isLogin);
            response.sendRedirect(clientURL + "?token=" + token);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
