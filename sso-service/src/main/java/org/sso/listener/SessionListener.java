package org.sso.listener;

import org.sso.context.SessionContext;
import org.sso.utils.LogoutUtils;
import org.sso.utils.RedisUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 对session的创建与销毁进行监听
 * */
public class SessionListener implements HttpSessionListener{

    private SessionContext sessionContext = SessionContext.getInstance();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        sessionContext.addSession(session);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();            // 当一个session销毁时，获取其中的Token
        String token = (String) session.getAttribute("loginFlag");
        if (RedisUtils.hasKey(token)){
            LogoutUtils.notifyClient(token);                            // 如果Token在Redis是存在的，销毁注册的子系统
        }
        sessionContext.delSession(session);
    }
}
