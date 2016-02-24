package com.jhhc.baseframework.web.user;

import com.jhhc.baseframework.web.core.Root;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author yecq
 */
public class LoginSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        HttpSession session = hse.getSession();
        if (session != null) {
            Object o = session.getAttribute("username");
            if (o != null) {
                String username = (String) o;
                Login login = Root.getInstance().getBean(LoginManager.class).getLogin(username);
                if (login != null) {
                    login.logout();
                }
            }
        }
    }
}
