package com.jhhc.baseframework.web.service;

import java.sql.SQLException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 *
 * @author yecq
 */
@Component("sret_wrap_advice")
class SretWrapAdvice {

    public Sret wrapSret(ProceedingJoinPoint p) {
        Sret sr = new Sret();
        try {
            sr = (Sret) p.proceed();
        } catch (Throwable e) {
            // 其他对象发出来的错误
            Class cls = e.getClass();
            if (cls.equals(IllegalArgumentException.class) || cls.equals(IllegalStateException.class)
                    || cls.equals(UnsupportedOperationException.class)) {
                sr.setFail(e.getMessage());
            } else if (e instanceof SQLException || e instanceof DataAccessException) {
                // 数据库发出的错误
                sr.setError("(数据库)" + e.getMessage());
            } else {
                sr.setError(e.getMessage() == null ? e.getClass().toString() : e.getMessage() + "");
            }
        }
        return sr;
    }
}
