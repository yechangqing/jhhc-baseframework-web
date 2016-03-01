package com.jhhc.baseframework.web.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用于jsp返回的打印，方法返回String
 *
 * @author yecq
 */
@Component("log_print_jsp_string_advice")
public class LogPrintJspStringAdvice {

    private static Logger log = Logger.getLogger(LogPrintJspStringAdvice.class);

    public String print(ProceedingJoinPoint p) {
        String mapping = "";    // @RequestMapping的值
        String jsp = null;        // jsp文件名，不包含后缀
        String param = "";          // 路径中的含有@RequestParam的参数
        String msg = "";

        try {
            // 获取所在类的注解值
            MethodSignature sign = (MethodSignature) p.getSignature();
            Annotation con = sign.getDeclaringType().getAnnotation(RequestMapping.class);
            if (con != null) {
                mapping += ((RequestMapping) con).value()[0];
            }
            Method m = sign.getMethod();

            //不会写不含有@ResponseBody的aspectj，只能这里排除了
            if (m.getAnnotation(ResponseBody.class) == null) {
                // 获取注解的参数
                RequestMapping rm = m.getAnnotation(RequestMapping.class);
                if (rm != null && rm.value().length > 0) {
                    mapping += rm.value()[0];
                }
                Class[] mcls = m.getParameterTypes();       // 1.7用这个方法
                Object[] args = p.getArgs();
                Annotation[][] annos = m.getParameterAnnotations();
                for (int i = 0; i < mcls.length; i++) {
                    Annotation[] ano = annos[i];
                    for (int j = 0; j < ano.length; j++) {
                        Class anoClass = ano[j].annotationType();
                        if (anoClass.equals(RequestParam.class)) {
                            RequestParam rp = (RequestParam) ano[j];
                            if (args[i] instanceof MultipartFile) {
                                MultipartFile f = (MultipartFile) args[i];
                                param += "上传文件" + f.getOriginalFilename() + ",大小" + f.getSize() + ", ";
                            } else {
                                param += rp.value() + "=" + args[i] + ", ";
                            }
                        } else if (anoClass.equals(PathVariable.class)) {
                            PathVariable path1 = (PathVariable) ano[j];
                            param += "PathVariable{" + path1.value() + "}=" + args[i] + ", ";
                        } else {

                        }
                    }
                }

            }
            msg += mapping;
            if (!param.equals("")) {
                msg += ", " + param.substring(0, param.length() - 2);
            }
        } catch (Throwable ex) {
            // 不要因为前面出错了而影响方法的执行
            msg = "";
        }

        try {
            jsp = (String) p.proceed();
        } catch (Throwable ex) {
            // 此处暂时还不知如何操作，只抛出异常
            msg += ",  jsp处理中产生错误\n" + ex.getMessage() + "  ==> error";
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        // 打印日志信息
        log.info(msg + ", 请求" + jsp + ".jsp,  ==> ok");

        return jsp;
    }
}
