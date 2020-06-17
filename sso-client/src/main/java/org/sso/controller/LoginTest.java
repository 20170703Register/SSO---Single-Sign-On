package org.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sso.context.SessionContext;
import org.sso.utils.CodeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginTest {


    /**
     * 登录成功111
     * */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
    public String success(){
        return "welcome1111";
    }


    /**
     * 登出成功
     * */
    @RequestMapping(value = "/index", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
    public @ResponseBody String index(){
        return "登出成功！";
    }


    /**
     * 登出
     * */
    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String isLogin = (String) session.getAttribute("loginFlag");    // 获取登录标识（局部会话）
        if (isLogin == null) {
            return "forward:/index";
        }

        String url = request.getRequestURL().toString();
        String clientURL = CodeUtils.url(url.substring(0, url.indexOf("/logout")));
        String token = CodeUtils.url(isLogin);

        String logoutURL = "http://localhost:8081/sso-service/logout?client=" + clientURL + "&token=" + token;
        return "redirect:" + logoutURL;                                     // 重定向到认证中，请求认证中心登出
    }


    /**
     * 认证中心的登出请求，用于销毁当前系统的session
     * */
    @RequestMapping(value = "/serviceLogout", method = RequestMethod.POST)
    public void serviceLogout(HttpServletRequest request) {
        String sessionId = request.getParameter("sessionId");
        SessionContext sessionContext = SessionContext.getInstance();
        HttpSession session = sessionContext.getSession(sessionId);
        session.invalidate();                                               // 销毁session，就是销毁登录标识（局部会话）
    }
}
