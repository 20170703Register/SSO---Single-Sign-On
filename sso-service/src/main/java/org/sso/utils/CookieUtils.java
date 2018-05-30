package org.sso.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具
 * */
public class CookieUtils {


    /**
     * 获取cookie中的值
     * */
    public static String get(Cookie[] cookies, String key){
        if (cookies == null){
            return null;
        }
        String value = null;
        for (int i = 0; i < cookies.length; i ++){
            if (key.equals(cookies[i].getName())){
                value = cookies[i].getValue();
                break;
            }
        }
        return value;
    }

    /**
     * 删除cookie
     * */
    public static void del(HttpServletResponse response, String key){
        if (key == null){
            return;
        }
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * 设置cookie
     * */
    public static void set(HttpServletResponse response, String key, String value){
        if (key == null){
            return;
        }
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
