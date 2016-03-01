package com.jhhc.baseframework.web.controller.restful;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 转换一下get方法的url中的转义字符，是个Before
 *
 * @author yecq
 */
@Component("param_decode_get_advice")
public class ParamDecode4GetAdvice {

    private static Logger log = Logger.getLogger(ParamDecode4GetAdvice.class);

    public HttpEntity decode(ProceedingJoinPoint p) throws Throwable {
        Object[] args = p.getArgs();
        MethodSignature sign = (MethodSignature) p.getSignature();
        Method m = sign.getMethod();
        RequestMapping rm = m.getAnnotation(RequestMapping.class);
        if (rm != null) {
            RequestMethod[] requestm = rm.method();
            if (requestm.length == 1 && requestm[0].equals(RequestMethod.GET)) {
                // 取出参数的值
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof HttpServletRequest) {
                        HttpServletRequest req = (HttpServletRequest) args[i];
                        Map<String, String[]> param = req.getParameterMap();
                        Iterator<Entry<String, String[]>> ite1 = param.entrySet().iterator();
                        while (ite1.hasNext()) {
                            Entry<String, String[]> ent1 = ite1.next();
                            String[] vs = ent1.getValue();
                            try {
                                for (int k = 0; k < vs.length; k++) {
                                    vs[k] = URLDecoder.decode(vs[k], "utf-8");
                                    log.debug("已解码数据" + ent1.getKey() + "为" + vs[k]);
                                }
                            } catch (UnsupportedEncodingException ex) {
                                log.error("解码数据" + ent1.getKey() + "出错");
                            }
                        }
                    }
                }

                Annotation[][] annos = m.getParameterAnnotations();
                for (int i = 0; i < args.length; i++) {
                    Annotation[] ano = annos[i];
                    for (int j = 0; j < ano.length; j++) {
                        Class anoClass = ano[j].annotationType();
                        if (anoClass.equals(RequestParam.class)) {
                            // 把变量值解码
                            RequestParam rp = (RequestParam) ano[j];
                            if (args[i].getClass().equals(String.class)) {
                                try {
                                    args[i] = URLDecoder.decode((String) args[i], "utf-8");
                                    // 这个地方实在不会替换参数值，先放着吧
                                    log.debug("已解码数据" + rp.value() + "为" + args[i]);
                                } catch (UnsupportedEncodingException ex) {
                                    log.error("解码数据" + rp.value() + "出错");
                                }
                            }
                        }
                    }
                }
            }
        }

        // 处理参数替换
        return (HttpEntity) p.proceed(args);

    }
}
