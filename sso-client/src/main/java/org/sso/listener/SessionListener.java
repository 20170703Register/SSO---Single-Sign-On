package org.sso.listener;

import org.sso.context.SessionContext;

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
        HttpSession session = httpSessionEvent.getSession();
        sessionContext.delSession(session);
    }
}
