package org.sso.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.sso.context.SessionContext;
import org.sso.utils.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 认证中心的Controller
 * */
@Controller
public class AuthenticationController {


    private static Map<String, String> userMap = new HashMap<>();

    static {
        userMap.put("小明", "12345");
        userMap.put("李明", "5678");
        userMap.put("张三", "x1234567");
    }


    /**
     * 返回登录页面
     * */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginPage(HttpServletResponse response){
        response.setHeader("Pragma", "no-cache");                       // 设置响应头，使登录页面不被浏览器缓存
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Cache-Control", "must-revalidate");
        return "login";
    }


    /**
     * 登录
     * */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String username, String password,
                        HttpServletRequest request, HttpServletResponse response,
                        RedirectAttributes redirectModel) {
        String clientURL = request.getParameter("client");

        if (userMap.containsKey(username)){                                 // 校验用户名与密码
            String value = userMap.get(username);
            if(!value.equals(password)){
                redirectModel.addFlashAttribute("msg", "密码错误！");
                return "redirect:/error?client=" + CodeUtils.url(clientURL);    // 密码错误重定向到登录页面
            }
        }else {
            redirectModel.addFlashAttribute("msg", "用户不存在！");
            return "redirect:/error?client=" + CodeUtils.url(clientURL);        // 用户不存在重定向到登录页面
        }

        Cookie[] cookies = request.getCookies();
        String tgc = CookieUtils.get(cookies, "TGC");                   // 获取全局会话TGC（这里的TGC就是全局会话的sessionid）
        String token = (String) SessionUtils.get(tgc, "loginFlag");
        if (token != null){                                                 // 如果Token存在说明有用户已登录
            String decode = CodeUtils.base64Decode(token);
            String name = decode.substring(decode.indexOf(".") + 1, decode.length());       // 得到已登录用户的名字
            if (name.equals(username)){
                if (!RedisUtils.hasKey(token)){
                    RedisUtils.addSet(token, "");
                }
                return "redirect:" + clientURL + "?token=" + CodeUtils.url(token);          // 如果与已登录用户的名字一样返回到子系统
            }else{
                if (RedisUtils.hasKey(token)){
                    LogoutUtils.notifyClient(token);                                        // 如果与已登录用户的名字不一样，注销已登录用户
                }
            }
        }

        String uuid = UUID.randomUUID().toString();
        token = CodeUtils.base64(uuid + "." + username);                   // 生成新Token，这里的Token是uuid加上用户名，并使用base64编码

        if (token != null){
            RedisUtils.addSet(token, "");                                       // 把Token存进Redis中，作为set数据类型的key
            HttpSession session = request.getSession();
            session.setAttribute("loginFlag", token);                        // 把Token保存到session中，标识该用户在认证中心登录
            int expTime = 60*2;
            session.setMaxInactiveInterval(expTime);                            // 设置登录用户的过期时间（这里设置为两分钟）
            CookieUtils.set(response, "TGC", session.getId());              // 把标识用户在认证中心登录的sessionId添加到Cookie中
        }
        return "redirect:" + clientURL + "?token=" + CodeUtils.url(token);      // 重定向到子系统
    }


    /**
     * 校验token
     * */
    @RequestMapping(value = "/token", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
    public @ResponseBody String token(HttpServletRequest request) {
        String token = request.getParameter("token");
        boolean isExist = RedisUtils.hasKey(token);
        if (isExist){                                                                   // 判断Redis中是否存在该token
            String decode = CodeUtils.base64Decode(token);
            String name = decode.substring(decode.indexOf(".") + 1, decode.length());   // 得到Token中的用户名

            String clientURL = request.getParameter("client");
            String sessionId = request.getParameter("sessionId");
            String url = clientURL + "/serviceLogout?sessionId=" +sessionId;
            RedisUtils.addSet(token, url);  // 注册子系统系统，把需要校验Token的子系统URL添加到Redis中，用于登出系统时通过该URL来访问子系统并销毁子系统的session
            return name;                    // 返回用户名给子系统
        } else {
            return "Not";                   // Token不存在
        }
    }


    /**
     * 登出
     * */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String clientURL = request.getParameter("client");
        String token = request.getParameter("token");
        boolean isExist = RedisUtils.hasKey(token);
        if (!isExist){
            return "redirect:" + clientURL + "/index";                      // Token不存在，直接登出成功
        }

        LogoutUtils.notifyClient(token);                                    // 销毁注册的子系统
        Cookie[] cookies = request.getCookies();
        String tgc = CookieUtils.get(cookies, "TGC");                   // 获取全局会话TGC（这里的TGC就是全局会话的sessionid）
        SessionContext context = SessionContext.getInstance();
        HttpSession tgcSession = context.getSession(tgc);
        tgcSession.invalidate();                                            // 销毁全局会话session
        CookieUtils.del(response, "TGC");                               // 删除TGC（全局会话）的Cookie

        return "redirect:" + clientURL + "/index";                          // 登出成功返回到子系统的index
    }

}
