package com.jhhc.baseframework.web.controller.restful;

import com.jhhc.baseframework.web.service.Sret;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * rest请求风格日志消息打印
 *
 * @author yecq
 */
@Component("log_print_restful_advice")
public class LogPrintRestfulAdvice {

    private static Logger log = Logger.getLogger(LogPrintRestfulAdvice.class);

//    @Around("Pointcut2.inControllerPackage() && Pointcut2.methodOfController()")
    public HttpEntity print(ProceedingJoinPoint p) {
        String mapping = "RESTful: ";    // @RequestMapping的值
        String param = "";       // @RequestParam的参数值
        String message = "";
        try {
            // 获取所在类的注解值
            MethodSignature sign = (MethodSignature) p.getSignature();
            Annotation con = sign.getDeclaringType().getAnnotation(RequestMapping.class);
            if (con != null) {
                mapping += ((RequestMapping) con).value()[0];
            }

            Method m = sign.getMethod();
            // 获取注解的参数
            RequestMapping rm = m.getAnnotation(RequestMapping.class);
            if (rm != null && rm.value().length > 0) {
                mapping += rm.value()[0];
            }
            if (rm.method() != null && rm.method().length > 0) {
                mapping += ", " + rm.method()[0];
            }
//        Parameter[] para = m.getParameters();   // 1.8才有该方法
//            Class[] mcls = m.getParameterTypes();       // 1.7用这个方法
            Object[] args = p.getArgs();

            // 先看一下是否有HttpServletRequest
            boolean have = false;
            String requestParam = "";
            for (int i = 0; i < args.length; i++) {
                if (args[i].getClass().equals(HttpServletRequest.class)) {
                    have = true;
                    // 处理request里的参数
                    HttpServletRequest req = (HttpServletRequest) args[i];
                    Enumeration<String> enu1 = req.getParameterNames();
                    while (enu1.hasMoreElements()) {
                        String k1 = enu1.nextElement();
                        requestParam += k1 + "=" + req.getParameter(k1) + ", ";
                    }
                    break;
                }
            }
            Annotation[][] annos = m.getParameterAnnotations();
            for (int i = 0; i < args.length; i++) {
                Annotation[] ano = annos[i];
                for (int j = 0; j < ano.length; j++) {
                    Class anoClass = ano[j].annotationType();
                    if (anoClass.equals(RequestParam.class) && !have) {
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

            message += mapping;
            param += requestParam;
            if (!param.equals("")) {
                message += ", " + param.substring(0, param.length() - 2);
            }
        } catch (Throwable ex) {
            // 不要因为前面出错了而影响方法的执行
            message = "";
        }

        HttpEntity ret = null;
        Object ori = null;    // 一开始的返回值
        try {
            ori = p.proceed();
        } catch (Throwable ex) {
            HttpHeaders header = new HttpHeaders();
            if (ex.getClass().equals(IllegalArgumentException.class) || ex.getClass().equals(IllegalStateException.class)
                    || ex.getClass().equals(UnsupportedOperationException.class)) {
                header.add("status", "fail");
            } else {
                header.add("status", "error");
            }
            header.add("message", ex.getMessage() + "");
            ret = new ResponseEntity(ex.getMessage(), header, HttpStatus.OK);
        }
        if (ret == null) {
            if (ori instanceof HttpEntity) {
                ret = (HttpEntity) ori;
            } else if (ori.getClass().equals(Sret.class)) {
                // 是Sret对象
                HttpHeaders hh1 = new HttpHeaders();
                Sret sr1 = (Sret) ori;
                hh1.add("status", sr1.getStatus());
                hh1.add("message", sr1.getMessage());
                ret = new ResponseEntity(sr1.getData(), hh1, HttpStatus.OK);
            } else {
                // 一般对象
                HttpHeaders hh1 = new HttpHeaders();
                hh1.add("status", "ok");
                hh1.add("message", "ok");
                ret = new ResponseEntity(ori, hh1, HttpStatus.OK);
            }
        }
        // 获取返回值
        HttpHeaders head = ret.getHeaders();
        String status = head.get("status").get(0);
        message += "   ==> " + status;
        if (!status.equals("ok")) {
            message += "," + head.get("message").get(0);
        }
        if (status.equals("error")) {
            log.error(message);
        } else {
            log.info(message);
        }
        return ret;
    }
}
