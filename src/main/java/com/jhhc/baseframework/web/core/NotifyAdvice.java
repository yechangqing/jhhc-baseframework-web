package com.jhhc.baseframework.web.core;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author yecq
 */
@Component("notify_advice")
public class NotifyAdvice {

    @Autowired
    private CoreChangeNotifier notifier;

    public void notifyChange(JoinPoint p) {
        //好办法
        MethodSignature sign = (MethodSignature) p.getSignature();
        Method m = sign.getMethod();
        if (m.isAnnotationPresent(Notify.class)) {
            // 获取注解的参数
            Notify noti = m.getAnnotation(Notify.class);
            String[] names = noti.value();
            notifier.fireCoreChange(names);
        }

        // 笨办法
//        // 获取主类的class
//        Class cls = p.getTarget().getClass();
//        Method[] methods = cls.getMethods();
//        for (int i = 0; i < methods.length; i++) {
//            Method m = methods[i];
//            if (m.isAnnotationPresent(Notify.class)) {
//                // 获取注解的参数
//                Notify noti = m.getAnnotation(Notify.class);
//                String[] names = noti.value();
//                for (int j = 0; j < names.length; j++) {
//                    System.out.println("\t" + names[j]);
//                }
//            }
//        }
    }
}
