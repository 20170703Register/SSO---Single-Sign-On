package org.sso.utils;

import org.sso.context.SessionContext;

import javax.servlet.http.HttpSession;

/**
 * session工具
 * */
public class SessionUtils {

    /**
     * 获取session中的值
     * */
    public static Object get(String id, String key){
        Object value = null;
        SessionContext context = SessionContext.getInstance();
        HttpSession session = context.getSession(id);
        if (session != null) {
            value = session.getAttribute(key);
        }
        return value;
    }
}
