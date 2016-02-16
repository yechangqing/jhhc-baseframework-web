package com.jhhc.baseframework.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

/**
 *
 * @author yecq
 */
@Component("logged_advice")
//@Aspect
//@Order(3)
public class LoggedAdvice {

//    @Before("Pointcut2.inControllerPackage() && Pointcut2.needLogged()")
    public void checkLogin(JoinPoint p) {
        Object[] o = p.getArgs();
        HttpSession session = null;
        for (int i = 0; i < o.length; i++) {
            if (o[i] instanceof HttpSession) {
                session = (HttpSession) o[i];
                break;
            } else if (o[i] instanceof HttpServletRequest) {
                session = ((HttpServletRequest) o[i]).getSession();
                break;
            }
        }
        if (session == null || session.getAttribute("username") == null) {
            throw new IllegalStateException("用户未登录");
        }
    }
}
